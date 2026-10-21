package systems.grebe.performance.mcp.visualvm;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

import systems.grebe.performance.mcp.support.CommandRunner;
import systems.grebe.performance.mcp.settings.VisualVmSettings;
import systems.grebe.performance.mcp.java.JavaEnvironment;
import systems.grebe.performance.mcp.java.ToolDownloads;

/** Findet bzw. installiert die VisualVM-Oberfläche und startet sie mit Befehlszeilenoptionen. */
public final class VisualVmLauncher {

    private final String home;
    private final boolean autoDownload;
    private final Path jdk;
    private final Supplier<ToolDownloads> downloads;

    /** @param jdk JDK, mit dem VisualVM läuft ({@code --jdkhome}), z.B. {@link JavaEnvironment#jdkHome()} */
    public VisualVmLauncher(VisualVmSettings c, Path jdk, ToolDownloads downloads) {
        this(c, jdk, () -> downloads);
    }

    public VisualVmLauncher(VisualVmSettings c, Path jdk, Supplier<ToolDownloads> downloads) {
        this.home = c.visualvmHome();
        this.autoDownload = c.autoDownload();
        this.jdk = jdk;
        this.downloads = downloads;
    }

    public Path executable() {
        Path dir;
        if (home != null) {
            dir = Path.of(home);
        } else if (autoDownload) {
            Path zip = downloads.get().get(ToolDownloads.Artifact.VISUALVM);
            dir = downloads.get().unzipOnce(zip, "visualvm_222").resolve("visualvm_222");
        } else {
            throw new IllegalStateException("VisualVM ist nicht konfiguriert (Installationsverzeichnis oder automatischer Download).");
        }
        String name = CommandRunner.WINDOWS ? "visualvm.exe" : "visualvm";
        Path exe = dir.resolve("bin").resolve(name);
        if (!Files.isRegularFile(exe)) {
            throw new IllegalStateException("VisualVM-Starter nicht gefunden: " + exe);
        }
        if (!CommandRunner.WINDOWS) {
            exe.toFile().setExecutable(true, false);
        }
        return exe;
    }

    /** Startet VisualVM (bzw. übergibt die Optionen an eine laufende Instanz) ohne zu warten. */
    public String launch(List<String> options) {
        Path exe = executable();
        List<String> cmd = new ArrayList<>(List.of(exe.toString(), "--jdkhome", jdk.toString()));
        cmd.addAll(options);
        try {
            new ProcessBuilder(cmd).redirectErrorStream(true)
                    .redirectOutput(ProcessBuilder.Redirect.DISCARD).start();
        } catch (java.io.IOException e) {
            throw new IllegalStateException("VisualVM konnte nicht gestartet werden: " + e.getMessage(), e);
        }
        return "VisualVM gestartet: " + String.join(" ", options).toLowerCase(Locale.ROOT).replace("--", "");
    }
}
