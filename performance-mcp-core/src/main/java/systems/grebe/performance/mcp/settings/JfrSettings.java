package systems.grebe.performance.mcp.settings;

/**
 * Einstellungen des Flight Recorders ({@code jfr_*}). devtools-Modul {@code jfr}: {@code settings} →
 * {@link #settings} (Standard {@code profile}; {@code profile} = feinere Samples, ca. 2 % Overhead, {@code default} =
 * Dauerbetrieb, &lt; 1 %), {@code maxSeconds} → {@link #maxSeconds} (Standard 300, mindestens 5).
 *
 * @param settings   Standard-Profil
 * @param maxSeconds max. Dauer für {@code jfr_record} in Sekunden
 */
public record JfrSettings(String settings, int maxSeconds) {

    public JfrSettings {
        settings = settings == null || settings.isBlank() ? "profile" : settings;
        maxSeconds = Math.max(5, maxSeconds);
    }

    public static JfrSettings defaults() {
        return new JfrSettings("profile", 300);
    }
}
