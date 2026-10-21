package systems.grebe.performance.mcp.java;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import systems.grebe.performance.mcp.support.CommandRunner;
import systems.grebe.performance.mcp.container.ContainerAccess;
import systems.grebe.performance.mcp.container.CliContainerAccess;
import systems.grebe.performance.mcp.settings.ContainerSettings;
import systems.grebe.performance.mcp.settings.JavaSettings;

/** Ausgewertete Grundeinstellungen + Zugriff auf Prozesse, Container, JMX und Artefakte. */
public final class JavaEnvironment {

    public record JmxTarget(String alias, String host, int port) {
        public String address() {
            return host + ":" + port;
        }
    }

    private final Path homeDir;
    private final Path jdkHome;
    private final ArtifactStore artifacts;
    private final ToolDownloads downloads;
    private final LocalJvms processes;
    private final Containers containers;
    private final Map<String, JmxTarget> jmxTargets = new LinkedHashMap<>();
    private final String jmxUser;
    private final String jmxPassword;
    private final boolean invasive;
    private final Pattern debugHosts;
    private final int maxLines;

    /** Mit Container-Zugriff über Docker/Podman-CLI mit Standardwerten. */
    public JavaEnvironment(JavaSettings c) {
        this(c, new CliContainerAccess(ContainerSettings.defaults()));
    }

    public JavaEnvironment(JavaSettings c, ContainerAccess containerAccess) {
        this.homeDir = c.homeDir().toAbsolutePath().normalize();
        this.jdkHome = c.jdkHome() != null ? Path.of(c.jdkHome()) : Path.of(System.getProperty("java.home"));
        Path dir = c.artifactDir() != null ? Path.of(c.artifactDir()) : homeDir.resolve("diagnostics");
        this.artifacts = ArtifactStore.forDirectory(dir, c.maxArtifacts());
        this.downloads = new ToolDownloads(homeDir.resolve("tools"));
        this.processes = new LocalJvms(regex(c.includeProcesses()), regex(c.excludeProcesses()), jcmd(), c.excludeSelf());
        this.containers = new Containers(containerAccess);
        for (String line : c.jmxTargets()) {
            int eq = line.indexOf('=');
            int colon = line.lastIndexOf(':');
            if (eq > 0 && colon > eq) {
                try {
                    String alias = line.substring(0, eq).trim();
                    jmxTargets.put(alias, new JmxTarget(alias, line.substring(eq + 1, colon).trim(),
                            Integer.parseInt(line.substring(colon + 1).trim())));
                } catch (NumberFormatException ignored) {
                    // ungültige Zeile
                }
            }
        }
        this.jmxUser = c.jmxUser();
        this.jmxPassword = c.jmxPassword();
        this.invasive = c.allowInvasive();
        this.debugHosts = regex(c.debugHosts());
        this.maxLines = Math.max(50, c.maxOutputLines());
    }

    private static Pattern regex(String s) {
        if (s == null || s.isBlank()) {
            return null;
        }
        try {
            return Pattern.compile(s);
        } catch (PatternSyntaxException e) {
            throw new IllegalStateException("Ungültiger regulärer Ausdruck in den Java-Grundeinstellungen: " + s);
        }
    }

    /** Basisverzeichnis des Hosts (Artefakte, Werkzeug-Downloads). */
    public Path homeDir() {
        return homeDir;
    }

    /** Downloads externer Werkzeuge (async-profiler, VisualVM) nach {@code <homeDir>/tools}. */
    public ToolDownloads downloads() {
        return downloads;
    }

    public Path jdkHome() {
        return jdkHome;
    }

    public Path jdkTool(String name) {
        return jdkHome.resolve("bin").resolve(CommandRunner.WINDOWS ? name + ".exe" : name);
    }

    public Path jcmd() {
        return jdkTool("jcmd");
    }

    public boolean jcmdAvailable() {
        return Files.isRegularFile(jcmd());
    }

    public ArtifactStore artifacts() {
        return artifacts;
    }

    public LocalJvms processes() {
        return processes;
    }

    public Containers containers() {
        return containers;
    }

    public Map<String, JmxTarget> jmxTargets() {
        return jmxTargets;
    }

    public String jmxUser() {
        return jmxUser;
    }

    public String jmxPassword() {
        return jmxPassword;
    }

    public boolean invasiveAllowed() {
        return invasive;
    }

    public void requireInvasive(String what) {
        if (!invasive) {
            throw new IllegalStateException(what + " ist eine invasive Operation und in den Java-Grundeinstellungen deaktiviert.");
        }
    }

    public boolean debugHostAllowed(String host) {
        return debugHosts == null || debugHosts.matcher(host).matches();
    }

    public int maxLines() {
        return maxLines;
    }

    /** Löst eine Zieladresse ({@code pid}, Namensteil, {@code container:…}, {@code jmx:…}) auf. */
    public JvmTarget target(String target) {
        return JvmTarget.resolve(this, target);
    }
}
