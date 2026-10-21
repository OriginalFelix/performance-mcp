package systems.grebe.performance.mcp.settings;

import java.util.List;

/**
 * Einstellungen der JVM-Diagnose ({@code jvm_*}). devtools-Modul {@code jvm}: {@code allowedCommands} →
 * {@link #allowedCommands} (Zeilen, Standard siehe {@link #DEFAULT_ALLOWED_COMMANDS}).
 *
 * @param allowedCommands jcmd-Befehle, die {@code jvm_jcmd} frei aufrufen darf
 */
public record JvmSettings(List<String> allowedCommands) {

    public static final List<String> DEFAULT_ALLOWED_COMMANDS = List.of("VM.version", "VM.flags", "VM.command_line",
            "VM.system_properties", "VM.uptime", "VM.info", "VM.classloader_stats", "VM.metaspace", "VM.native_memory",
            "VM.dynlibs", "Thread.print", "GC.heap_info", "GC.class_histogram", "GC.finalizer_info", "Compiler.codecache",
            "Compiler.queue", "JFR.check", "VM.log", "Compiler.CodeHeap_Analytics", "System.map", "VM.events",
            "Thread.dump_to_file");

    public JvmSettings {
        allowedCommands = allowedCommands == null ? DEFAULT_ALLOWED_COMMANDS : List.copyOf(allowedCommands);
    }

    public static JvmSettings defaults() {
        return new JvmSettings(DEFAULT_ALLOWED_COMMANDS);
    }
}
