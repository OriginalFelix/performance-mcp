package systems.grebe.performance.mcp.server;

import java.nio.file.Path;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import systems.grebe.performance.mcp.settings.AsprofSettings;
import systems.grebe.performance.mcp.settings.ContainerSettings;
import systems.grebe.performance.mcp.settings.DebugSettings;
import systems.grebe.performance.mcp.settings.JavaSettings;
import systems.grebe.performance.mcp.settings.JfrSettings;
import systems.grebe.performance.mcp.settings.JvmSettings;
import systems.grebe.performance.mcp.settings.VisualVmSettings;

/**
 * Konfiguration ({@code application.yml}, Umgebungsvariablen {@code PERFORMANCE_*}, Kommandozeile
 * {@code --performance.…}). Die Bereiche entsprechen den Einstellungs-Records des SDK; Standardwerte wie dort.
 *
 * @param home Basisverzeichnis für Artefakte ({@code diagnostics/}) und Werkzeug-Downloads ({@code tools/});
 *             leer = {@code ~/.performance-mcp}
 */
@ConfigurationProperties("performance")
public record PerformanceProperties(Path home,
                                    @DefaultValue Java java,
                                    @DefaultValue Jvm jvm,
                                    @DefaultValue Jfr jfr,
                                    @DefaultValue Asprof asprof,
                                    @DefaultValue Visualvm visualvm,
                                    @DefaultValue Debug debug,
                                    @DefaultValue Container container) {

    /** Java-Grundeinstellungen ({@code performance.java.*}). */
    public record Java(String jdkHome,
                       String artifactDir,
                       @DefaultValue("50") int maxArtifacts,
                       String includeProcesses,
                       @DefaultValue(JavaSettings.DEFAULT_EXCLUDE) String excludeProcesses,
                       List<String> jmxTargets,
                       String jmxUser,
                       String jmxPassword,
                       @DefaultValue(JavaSettings.DEFAULT_DEBUG_HOSTS) String debugHosts,
                       @DefaultValue("true") boolean allowInvasive,
                       @DefaultValue("400") int maxOutputLines,
                       @DefaultValue("true") boolean excludeSelf) {
    }

    /** {@code performance.jvm.*}: jvm_*-Tools. */
    public record Jvm(@DefaultValue("true") boolean enabled, List<String> allowedCommands) {
    }

    /** {@code performance.jfr.*}: jfr_*-Tools. */
    public record Jfr(@DefaultValue("true") boolean enabled, @DefaultValue("profile") String settings,
                      @DefaultValue("300") int maxSeconds) {
    }

    /** {@code performance.asprof.*}: asprof_*-Tools. */
    public record Asprof(@DefaultValue("true") boolean enabled, String home,
                         @DefaultValue("true") boolean autoDownload, @DefaultValue("300") int maxSeconds) {
    }

    /** {@code performance.visualvm.*}: visualvm_*-Tools. */
    public record Visualvm(@DefaultValue("true") boolean enabled, String home,
                           @DefaultValue("true") boolean autoDownload, @DefaultValue("20") int sampleIntervalMs) {
    }

    /** {@code performance.debug.*}: debug_*-Tools. */
    public record Debug(@DefaultValue("true") boolean enabled, @DefaultValue("120") int maxWaitSeconds,
                        @DefaultValue("2") int variableDepth) {
    }

    /** {@code performance.container.*}: Zugriff auf JVMs in Containern. */
    public record Container(@DefaultValue("true") boolean enabled, @DefaultValue("auto") String runtime,
                            @DefaultValue("docker") String dockerBinary, String dockerContext, String dockerHost,
                            @DefaultValue("podman") String podmanBinary, String podmanConnection, String podmanUrl,
                            @DefaultValue(".*") String allowedContainers) {
    }

    public JavaSettings javaSettings() {
        return new JavaSettings(home, java.jdkHome(), java.artifactDir(), java.maxArtifacts(), java.includeProcesses(),
                java.excludeProcesses(), java.jmxTargets(), java.jmxUser(), java.jmxPassword(), java.debugHosts(),
                java.allowInvasive(), java.maxOutputLines(), java.excludeSelf());
    }

    public JvmSettings jvmSettings() {
        return new JvmSettings(jvm.allowedCommands() == null || jvm.allowedCommands().isEmpty() ? null : jvm.allowedCommands());
    }

    public JfrSettings jfrSettings() {
        return new JfrSettings(jfr.settings(), jfr.maxSeconds());
    }

    public AsprofSettings asprofSettings() {
        return new AsprofSettings(asprof.home(), asprof.autoDownload(), asprof.maxSeconds());
    }

    public VisualVmSettings visualVmSettings() {
        return new VisualVmSettings(visualvm.home(), visualvm.autoDownload(), visualvm.sampleIntervalMs());
    }

    public DebugSettings debugSettings() {
        return new DebugSettings(debug.maxWaitSeconds(), debug.variableDepth());
    }

    public ContainerSettings containerSettings() {
        return new ContainerSettings(container.enabled(), container.runtime(), container.dockerBinary(),
                container.dockerContext(), container.dockerHost(), container.podmanBinary(), container.podmanConnection(),
                container.podmanUrl(), container.allowedContainers());
    }
}
