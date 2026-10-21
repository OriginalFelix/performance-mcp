package systems.grebe.performance.mcp.settings;

/**
 * Einstellungen des Debuggers ({@code debug_*}). devtools-Modul {@code debug}: {@code maxWaitSeconds} →
 * {@link #maxWaitSeconds} (Standard 120, mindestens 1), {@code variableDepth} → {@link #variableDepth} (Standard 2,
 * begrenzt auf 0..5).
 *
 * @param maxWaitSeconds max. Wartezeit auf einen Breakpoint
 * @param variableDepth  wie weit verschachtelte Objekte bei Variablen aufgeklappt werden
 */
public record DebugSettings(int maxWaitSeconds, int variableDepth) {

    public DebugSettings {
        maxWaitSeconds = Math.max(1, maxWaitSeconds);
        variableDepth = Math.max(0, Math.min(5, variableDepth));
    }

    public static DebugSettings defaults() {
        return new DebugSettings(120, 2);
    }
}
