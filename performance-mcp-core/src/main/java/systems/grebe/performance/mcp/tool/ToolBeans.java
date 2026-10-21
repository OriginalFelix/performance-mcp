package systems.grebe.performance.mcp.tool;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.metadata.ToolMetadata;

/**
 * Erzeugt Tool-Callbacks aus Objekten mit {@code @Tool}-Methoden wie {@link ToolCallbacks#from}, übernimmt aber
 * zusätzlich die {@link ToolHints} (als {@link Hints} abfragbar, z.B. für MCP-Tool-Annotations), und versieht
 * Tool-Namen auf Wunsch mit einem Präfix ({@code jvm_} + {@code processes}).
 */
public final class ToolBeans {

    private ToolBeans() {
    }

    /**
     * Hinweise eines Tools (Werte wie bei MCP-Tool-Annotations, {@code null} = keine Angabe).
     *
     * @param title optionaler Anzeigename
     */
    public record Hints(String title, Boolean readOnly, Boolean destructive, Boolean idempotent, Boolean openWorld) {

        /** Aus der Annotation; destructive/idempotent sind laut Spezifikation nur bei nicht lesenden Tools bedeutsam. */
        public static Hints of(ToolHints h) {
            return new Hints(null, h.readOnly(), h.readOnly() ? null : h.destructive(),
                    h.readOnly() ? null : h.idempotent(), h.openWorld());
        }
    }

    /** Ein Callback, der seinen Delegaten kennt (Hüllen für Hinweise und Präfix). */
    public interface Wrapper extends ToolCallback {
        ToolCallback delegate();
    }

    /** Callbacks aller {@code @Tool}-Methoden der Objekte, mit Hinweisen aus {@link ToolHints}. */
    public static List<ToolCallback> callbacks(Object... beans) {
        List<ToolCallback> out = new ArrayList<>();
        for (Object bean : beans) {
            Map<String, ToolHints> hints = hints(bean.getClass());
            for (ToolCallback cb : ToolCallbacks.from(bean)) {
                ToolHints h = hints.get(cb.getToolDefinition().name());
                out.add(h == null ? cb : new Hinted(cb, Hints.of(h)));
            }
        }
        return out;
    }

    /** Hinweise eines Callbacks (auch durch Hüllen hindurch) oder {@code null}. */
    public static Hints hints(ToolCallback cb) {
        if (cb instanceof Hinted h) {
            return h.hints;
        }
        if (cb instanceof Wrapper w) {
            return hints(w.delegate());
        }
        return null;
    }

    /** Name mit Präfix {@code <prefix>_}, falls er nicht schon damit beginnt. */
    public static String prefixed(String prefix, String name) {
        String p = prefix + "_";
        return name.startsWith(p) ? name : p + name;
    }

    /** Callback mit Namenspräfix ({@code prefix_name}); Hinweise bleiben über {@link #hints(ToolCallback)} erreichbar. */
    public static ToolCallback prefixed(String prefix, ToolCallback cb) {
        return new Prefixed(prefix, cb);
    }

    public static List<ToolCallback> prefixed(String prefix, List<ToolCallback> callbacks) {
        return callbacks.stream().map(cb -> prefixed(prefix, cb)).toList();
    }

    private static Map<String, ToolHints> hints(Class<?> type) {
        ToolHints classHints = type.getAnnotation(ToolHints.class);
        Map<String, ToolHints> out = new HashMap<>();
        for (Method m : type.getMethods()) {
            Tool tool = m.getAnnotation(Tool.class);
            if (tool == null) {
                continue;
            }
            ToolHints h = m.getAnnotation(ToolHints.class);
            if (h == null) {
                h = classHints;
            }
            if (h != null) {
                out.put(tool.name().isBlank() ? m.getName() : tool.name(), h);
            }
        }
        return out;
    }

    /** Callback mit Hinweisen; ansonsten unverändert. */
    private record Hinted(ToolCallback delegate, Hints hints) implements Wrapper {

        @Override
        public ToolDefinition getToolDefinition() {
            return delegate.getToolDefinition();
        }

        @Override
        public ToolMetadata getToolMetadata() {
            return delegate.getToolMetadata();
        }

        @Override
        public String call(String toolInput) {
            return delegate.call(toolInput);
        }

        @Override
        public String call(String toolInput, ToolContext toolContext) {
            return delegate.call(toolInput, toolContext);
        }
    }

    private static final class Prefixed implements Wrapper {

        private final ToolCallback delegate;
        private final ToolDefinition definition;

        Prefixed(String prefix, ToolCallback delegate) {
            this.delegate = delegate;
            ToolDefinition d = delegate.getToolDefinition();
            this.definition = ToolDefinition.builder()
                    .name(prefixed(prefix, d.name()))
                    .description(d.description())
                    .inputSchema(d.inputSchema())
                    .build();
        }

        @Override
        public ToolCallback delegate() {
            return delegate;
        }

        @Override
        public ToolDefinition getToolDefinition() {
            return definition;
        }

        @Override
        public ToolMetadata getToolMetadata() {
            return delegate.getToolMetadata();
        }

        @Override
        public String call(String toolInput) {
            return delegate.call(toolInput);
        }

        @Override
        public String call(String toolInput, ToolContext toolContext) {
            return delegate.call(toolInput, toolContext);
        }
    }
}
