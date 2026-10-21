package systems.grebe.performance.mcp.settings;

/**
 * Einstellungen von VisualVM ({@code visualvm_*}). devtools-Modul {@code visualvm}: {@code visualvmHome} →
 * {@link #visualvmHome}, {@code autoDownload} → {@link #autoDownload} (Standard {@code true}), {@code sampleIntervalMs}
 * → {@link #sampleIntervalMs} (Standard 20, mindestens 5).
 *
 * @param visualvmHome     VisualVM-Installation (mit {@code bin/visualvm}); {@code null} = automatisch laden
 * @param autoDownload     VisualVM 2.2.2 (SHA-256-geprüft) beim ersten Öffnen der Oberfläche laden
 * @param sampleIntervalMs Abstand der Thread-Dumps beim CPU-Sampler in Millisekunden
 */
public record VisualVmSettings(String visualvmHome, boolean autoDownload, int sampleIntervalMs) {

    public VisualVmSettings {
        visualvmHome = visualvmHome == null || visualvmHome.isBlank() ? null : visualvmHome;
    }

    public static VisualVmSettings defaults() {
        return new VisualVmSettings(null, true, 20);
    }
}
