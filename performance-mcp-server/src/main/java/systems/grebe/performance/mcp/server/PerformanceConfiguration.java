package systems.grebe.performance.mcp.server;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.mcp.McpToolUtils;
import org.springframework.ai.mcp.customizer.McpSyncServerCustomizer;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import systems.grebe.performance.mcp.PerformanceArea;
import systems.grebe.performance.mcp.PerformanceTools;
import systems.grebe.performance.mcp.container.CliContainerAccess;
import systems.grebe.performance.mcp.container.ContainerAccess;
import systems.grebe.performance.mcp.debug.DebugSessions;
import systems.grebe.performance.mcp.java.JavaEnvironment;
import systems.grebe.performance.mcp.tool.ToolBeans;

/**
 * Verdrahtet die SDK-Tools mit dem MCP-Server. Alle Bereiche und alle Tools (auch die invasiven) sind aktiv; es gibt
 * keine Berechtigungsprüfung. Einschränkungen laufen ausschließlich über die Konfiguration ({@code performance.*}).
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(PerformanceProperties.class)
public class PerformanceConfiguration {

    @Bean
    ContainerAccess containerAccess(PerformanceProperties p) {
        return new CliContainerAccess(p.containerSettings());
    }

    @Bean
    JavaEnvironment javaEnvironment(PerformanceProperties p, ContainerAccess containers) {
        return PerformanceTools.environment(p.javaSettings(), containers);
    }

    /** Offene Debug-Sitzungen; werden mit der Anwendung geschlossen (JDI-Verbindungen getrennt). */
    @Bean(destroyMethod = "close")
    DebugSessions debugSessions() {
        return new DebugSessions();
    }

    /** Alle Tool-Callbacks der aktiven Bereiche, mit Präfix ({@code jvm_processes} …). */
    @Bean
    PerformanceToolSet performanceTools(PerformanceProperties p, JavaEnvironment env, DebugSessions sessions) {
        Supplier<JavaEnvironment> supplier = () -> env;
        List<PerformanceArea> areas = new ArrayList<>();
        List<ToolCallback> tools = new ArrayList<>();
        if (p.jvm().enabled()) {
            areas.add(PerformanceArea.JVM);
            tools.addAll(PerformanceTools.prefixed(PerformanceArea.JVM,
                    PerformanceTools.jvmTools(supplier, p.jvmSettings())));
        }
        if (p.jfr().enabled()) {
            areas.add(PerformanceArea.JFR);
            tools.addAll(PerformanceTools.prefixed(PerformanceArea.JFR,
                    PerformanceTools.jfrTools(supplier, p.jfrSettings())));
        }
        if (p.asprof().enabled()) {
            areas.add(PerformanceArea.ASPROF);
            tools.addAll(PerformanceTools.prefixed(PerformanceArea.ASPROF,
                    PerformanceTools.asprofTools(supplier, p.asprofSettings())));
        }
        if (p.visualvm().enabled()) {
            areas.add(PerformanceArea.VISUALVM);
            tools.addAll(PerformanceTools.prefixed(PerformanceArea.VISUALVM,
                    PerformanceTools.visualVmTools(supplier, p.visualVmSettings())));
        }
        if (p.debug().enabled()) {
            areas.add(PerformanceArea.DEBUG);
            tools.addAll(PerformanceTools.prefixed(PerformanceArea.DEBUG,
                    PerformanceTools.debugTools(supplier, p.debugSettings(), sessions)));
        }
        return new PerformanceToolSet(List.copyOf(areas), List.copyOf(tools));
    }

    /**
     * Tool-Spezifikationen mit MCP-Tool-Annotations (readOnly …) aus den SDK-Hinweisen. Die Standard-Umwandlung von
     * Spring AI ist dafür abgeschaltet ({@code spring.ai.mcp.server.tool-callback-converter=false}).
     */
    @Bean
    List<McpServerFeatures.SyncToolSpecification> syncTools(PerformanceToolSet tools) {
        return tools.tools().stream().map(PerformanceConfiguration::specification).toList();
    }

    static McpServerFeatures.SyncToolSpecification specification(ToolCallback cb) {
        McpServerFeatures.SyncToolSpecification spec = McpToolUtils.toSyncToolSpecification(cb);
        ToolBeans.Hints h = ToolBeans.hints(cb);
        if (h == null) {
            return spec;
        }
        McpSchema.Tool t = spec.tool();
        McpSchema.Tool tool = new McpSchema.Tool(t.name(), t.title(), t.description(), t.inputSchema(),
                t.outputSchema(), new McpSchema.ToolAnnotations(h.title(), h.readOnly(), h.destructive(),
                        h.idempotent(), h.openWorld(), null), t.meta(), t.icons());
        return new McpServerFeatures.SyncToolSpecification(tool, spec.callHandler());
    }

    /** Instructions der aktiven Bereiche für {@code initialize}. */
    @Bean
    @Primary
    McpSyncServerCustomizer performanceInstructions(PerformanceToolSet tools) {
        String text = Instructions.build(tools.areas());
        return spec -> {
            spec.instructions(text);
            spec.immediateExecution(true);
        };
    }
}
