package systems.grebe.performance.mcp;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.ai.tool.ToolCallback;
import systems.grebe.performance.mcp.container.ContainerAccess;
import systems.grebe.performance.mcp.debug.DebugSessions;
import systems.grebe.performance.mcp.java.JavaEnvironment;
import systems.grebe.performance.mcp.settings.AsprofSettings;
import systems.grebe.performance.mcp.settings.DebugSettings;
import systems.grebe.performance.mcp.settings.JavaSettings;
import systems.grebe.performance.mcp.settings.JfrSettings;
import systems.grebe.performance.mcp.settings.JvmSettings;
import systems.grebe.performance.mcp.settings.VisualVmSettings;
import systems.grebe.performance.mcp.tool.ToolBeans;

import static org.assertj.core.api.Assertions.assertThat;

/** Fabrik-API: Tool-Namen je Bereich (identisch zu devtools-mcp), Hinweise, Präfix, Einstellungen. */
class PerformanceToolsTest {

    @TempDir
    Path home;

    private List<ToolCallback> allPrefixed() {
        JavaEnvironment env = PerformanceTools.environment(JavaSettings.defaults().withHomeDir(home), ContainerAccess.none());
        List<ToolCallback> all = new ArrayList<>();
        all.addAll(PerformanceTools.prefixed(PerformanceArea.JVM, PerformanceTools.jvmTools(() -> env, JvmSettings.defaults())));
        all.addAll(PerformanceTools.prefixed(PerformanceArea.JFR, PerformanceTools.jfrTools(() -> env, JfrSettings.defaults())));
        all.addAll(PerformanceTools.prefixed(PerformanceArea.ASPROF, PerformanceTools.asprofTools(() -> env, AsprofSettings.defaults())));
        all.addAll(PerformanceTools.prefixed(PerformanceArea.VISUALVM, PerformanceTools.visualVmTools(() -> env, VisualVmSettings.defaults())));
        all.addAll(PerformanceTools.prefixed(PerformanceArea.DEBUG,
                PerformanceTools.debugTools(() -> env, DebugSettings.defaults(), new DebugSessions())));
        return all;
    }

    @Test
    void toolNamesMatchDevtools() throws IOException {
        Set<String> expected = new TreeSet<>(List.of(new String(getClass().getResourceAsStream("/expected-tools.txt")
                .readAllBytes()).trim().split("\\s+")));
        Set<String> actual = new TreeSet<>();
        allPrefixed().forEach(cb -> actual.add(cb.getToolDefinition().name()));
        assertThat(actual).hasSize(34).isEqualTo(expected);
    }

    @Test
    void jvmReadAndInvasiveAreSeparate() {
        JavaEnvironment env = PerformanceTools.environment(JavaSettings.defaults().withHomeDir(home), ContainerAccess.none());
        assertThat(PerformanceTools.jvmReadTools(() -> env)).extracting(cb -> cb.getToolDefinition().name())
                .containsExactlyInAnyOrder("processes", "info", "threads", "heap", "native_memory");
        assertThat(PerformanceTools.jvmInvasiveTools(() -> env, JvmSettings.defaults()))
                .extracting(cb -> cb.getToolDefinition().name())
                .containsExactlyInAnyOrder("heap_dump", "gc_run", "jcmd");
    }

    @Test
    void readOnlyHintsSurvivePrefixing() {
        JavaEnvironment env = PerformanceTools.environment(JavaSettings.defaults().withHomeDir(home), ContainerAccess.none());
        ToolCallback processes = PerformanceTools.prefixed(PerformanceArea.JVM, PerformanceTools.jvmReadTools(() -> env)).stream()
                .filter(cb -> cb.getToolDefinition().name().equals("jvm_processes")).findFirst().orElseThrow();
        assertThat(ToolBeans.hints(processes).readOnly()).isTrue();
        assertThat(ToolBeans.hints(processes).openWorld()).isFalse();
        assertThat(ToolBeans.prefixed("jvm", "jvm_info")).isEqualTo("jvm_info");
    }

    @Test
    void invasiveOperationsFollowSettings() {
        JavaEnvironment env = PerformanceTools.environment(
                JavaSettings.defaults().withHomeDir(home).withAllowInvasive(false), ContainerAccess.none());
        ToolCallback gc = PerformanceTools.jvmInvasiveTools(() -> env, JvmSettings.defaults()).stream()
                .filter(cb -> cb.getToolDefinition().name().equals("gc_run")).findFirst().orElseThrow();
        assertThat(org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> gc.call("{}")).getMessage())
                .contains("invasive Operation");
    }

    @Test
    void areasCarryTexts() {
        assertThat(PerformanceArea.byId("jfr")).isEqualTo(PerformanceArea.JFR);
        assertThat(PerformanceArea.JVM.instructions()).contains("`jvm_processes` zuerst");
        assertThat(PerformanceArea.JVM.briefInstructions()).startsWith("jvm_processes zuerst");
        assertThat(PerformanceArea.ASPROF.briefInstructions()).isNull();
        assertThat(PerformanceArea.JAVA.hasTools()).isFalse();
        assertThat(PerformanceArea.DEBUG.enabledByDefault()).isFalse();
    }

    @Test
    void settingsDefaultsMatchDevtoolsSchema() {
        JavaSettings j = JavaSettings.defaults();
        assertThat(j.maxArtifacts()).isEqualTo(50);
        assertThat(j.maxOutputLines()).isEqualTo(400);
        assertThat(j.allowInvasive()).isTrue();
        assertThat(j.debugHosts()).isEqualTo("localhost|127\\.0\\.0\\.1");
        assertThat(JvmSettings.defaults().allowedCommands()).hasSize(22).contains("VM.uptime", "Thread.dump_to_file");
        assertThat(JfrSettings.defaults().settings()).isEqualTo("profile");
        assertThat(new JfrSettings("default", 1).maxSeconds()).isEqualTo(5);
        assertThat(new DebugSettings(0, 9)).isEqualTo(new DebugSettings(1, 5));
        assertThat(VisualVmSettings.defaults().sampleIntervalMs()).isEqualTo(20);
        assertThat(AsprofSettings.defaults().autoDownload()).isTrue();
    }

    @Test
    void debugSessionsCloseIsIdempotent() {
        DebugSessions s = new DebugSessions();
        s.close();
        s.close();
        assertThat(s.all()).isEmpty();
    }
}
