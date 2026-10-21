package systems.grebe.performance.mcp.settings;

/**
 * Einstellungen von async-profiler ({@code asprof_*}). devtools-Modul {@code asprof}: {@code asprofHome} →
 * {@link #asprofHome}, {@code autoDownload} → {@link #autoDownload} (Standard {@code true}), {@code maxSeconds} →
 * {@link #maxSeconds} (Standard 300, mindestens 5).
 *
 * @param asprofHome   entpacktes async-profiler-Verzeichnis (mit {@code bin/asprof}); {@code null} = automatisch laden
 * @param autoDownload async-profiler 4.5 (SHA-256-geprüft) von GitHub nach {@code <homeDir>/tools} laden
 * @param maxSeconds   max. Profildauer in Sekunden
 */
public record AsprofSettings(String asprofHome, boolean autoDownload, int maxSeconds) {

    public AsprofSettings {
        asprofHome = asprofHome == null || asprofHome.isBlank() ? null : asprofHome;
        maxSeconds = Math.max(5, maxSeconds);
    }

    public static AsprofSettings defaults() {
        return new AsprofSettings(null, true, 300);
    }
}
