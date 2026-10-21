package systems.grebe.performance.mcp.settings;

import java.nio.file.Path;
import java.util.List;

/**
 * Java-Grundeinstellungen: welche JVMs erreichbar sind und wohin Aufzeichnungen und Dumps geschrieben werden.
 * Leere Texte gelten als nicht gesetzt.
 *
 * <p>Entspricht den Feldern des devtools-Moduls „Java-Grundeinstellungen“ ({@code java}):
 * {@code jdkHome}→{@link #jdkHome}, {@code artifactDir}→{@link #artifactDir}, {@code maxArtifacts}→{@link #maxArtifacts},
 * {@code includeProcesses}→{@link #includeProcesses}, {@code excludeProcesses}→{@link #excludeProcesses},
 * {@code jmxTargets}→{@link #jmxTargets}, {@code jmxUser}→{@link #jmxUser}, {@code jmxPassword}→{@link #jmxPassword},
 * {@code debugHosts}→{@link #debugHosts}, {@code allowInvasive}→{@link #allowInvasive},
 * {@code maxOutputLines}→{@link #maxOutputLines}. Neu: {@link #homeDir} (statt {@code SettingsStore.defaultHome()})
 * und {@link #excludeSelf}.
 *
 * @param homeDir        Basisverzeichnis des Hosts: {@code diagnostics/} (Artefakte, falls {@code artifactDir} leer) und
 *                       {@code tools/} (Downloads von async-profiler und VisualVM); {@code null} = {@code ~/.performance-mcp}
 * @param jdkHome        JDK für jcmd/Attach; {@code null} = JDK der laufenden JVM
 * @param artifactDir    Ablage für JFR-Aufzeichnungen, Heap-Dumps, Flame Graphs; {@code null} = {@code <homeDir>/diagnostics}
 * @param maxArtifacts   Anzahl Artefakte, ältere werden gelöscht (Standard 50)
 * @param includeProcesses Regulärer Ausdruck auf Hauptklasse/Kommandozeile; {@code null} = alle JVMs des Benutzers
 * @param excludeProcesses Regulärer Ausdruck für auszuschließende Prozesse
 * @param jmxTargets     eine Angabe je Ziel: {@code alias=host:port}
 * @param jmxUser        JMX-Benutzer
 * @param jmxPassword    JMX-Passwort
 * @param debugHosts     Regulärer Ausdruck für Hosts, an deren JDWP-Port sich der Debugger hängen darf
 * @param allowInvasive  Heap-Dumps, GC erzwingen, beliebige jcmd-Befehle erlauben
 * @param maxOutputLines max. Ausgabezeilen (mindestens 50)
 * @param excludeSelf    die JVM des Hosts selbst nie als Ziel anbieten (devtools: immer {@code true})
 */
public record JavaSettings(Path homeDir, String jdkHome, String artifactDir, int maxArtifacts, String includeProcesses,
                           String excludeProcesses, List<String> jmxTargets, String jmxUser, String jmxPassword,
                           String debugHosts, boolean allowInvasive, int maxOutputLines, boolean excludeSelf) {

    public static final String DEFAULT_EXCLUDE =
            "com\\.intellij\\.idea\\.Main|org\\.gradle\\.launcher\\.daemon|org\\.jetbrains\\.";
    public static final String DEFAULT_DEBUG_HOSTS = "localhost|127\\.0\\.0\\.1";

    public JavaSettings {
        if (homeDir == null) {
            homeDir = Path.of(System.getProperty("user.home"), ".performance-mcp");
        }
        jdkHome = blankToNull(jdkHome);
        artifactDir = blankToNull(artifactDir);
        includeProcesses = blankToNull(includeProcesses);
        jmxTargets = jmxTargets == null ? List.of() : List.copyOf(jmxTargets);
        jmxUser = blankToNull(jmxUser);
        jmxPassword = blankToNull(jmxPassword);
    }

    /** Standardwerte wie im devtools-Modul. */
    public static JavaSettings defaults() {
        return new JavaSettings(null, null, null, 50, null, DEFAULT_EXCLUDE, List.of(), null, null, DEFAULT_DEBUG_HOSTS,
                true, 400, true);
    }

    public JavaSettings withHomeDir(Path v) {
        return new JavaSettings(v, jdkHome, artifactDir, maxArtifacts, includeProcesses, excludeProcesses, jmxTargets,
                jmxUser, jmxPassword, debugHosts, allowInvasive, maxOutputLines, excludeSelf);
    }

    public JavaSettings withJdkHome(String v) {
        return new JavaSettings(homeDir, v, artifactDir, maxArtifacts, includeProcesses, excludeProcesses, jmxTargets,
                jmxUser, jmxPassword, debugHosts, allowInvasive, maxOutputLines, excludeSelf);
    }

    public JavaSettings withArtifactDir(String v) {
        return new JavaSettings(homeDir, jdkHome, v, maxArtifacts, includeProcesses, excludeProcesses, jmxTargets,
                jmxUser, jmxPassword, debugHosts, allowInvasive, maxOutputLines, excludeSelf);
    }

    public JavaSettings withIncludeProcesses(String v) {
        return new JavaSettings(homeDir, jdkHome, artifactDir, maxArtifacts, v, excludeProcesses, jmxTargets,
                jmxUser, jmxPassword, debugHosts, allowInvasive, maxOutputLines, excludeSelf);
    }

    public JavaSettings withExcludeProcesses(String v) {
        return new JavaSettings(homeDir, jdkHome, artifactDir, maxArtifacts, includeProcesses, v, jmxTargets,
                jmxUser, jmxPassword, debugHosts, allowInvasive, maxOutputLines, excludeSelf);
    }

    public JavaSettings withJmxTargets(List<String> v) {
        return new JavaSettings(homeDir, jdkHome, artifactDir, maxArtifacts, includeProcesses, excludeProcesses, v,
                jmxUser, jmxPassword, debugHosts, allowInvasive, maxOutputLines, excludeSelf);
    }

    public JavaSettings withAllowInvasive(boolean v) {
        return new JavaSettings(homeDir, jdkHome, artifactDir, maxArtifacts, includeProcesses, excludeProcesses, jmxTargets,
                jmxUser, jmxPassword, debugHosts, v, maxOutputLines, excludeSelf);
    }

    public JavaSettings withExcludeSelf(boolean v) {
        return new JavaSettings(homeDir, jdkHome, artifactDir, maxArtifacts, includeProcesses, excludeProcesses, jmxTargets,
                jmxUser, jmxPassword, debugHosts, allowInvasive, maxOutputLines, v);
    }

    static String blankToNull(String s) {
        return s == null || s.isBlank() ? null : s;
    }
}
