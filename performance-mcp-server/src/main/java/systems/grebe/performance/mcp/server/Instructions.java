package systems.grebe.performance.mcp.server;

import java.util.List;

import systems.grebe.performance.mcp.PerformanceArea;

/** Baut die MCP-{@code instructions} aus den Texten der aktiven Bereiche. */
final class Instructions {

    static final String PREAMBLE = """
            # Performance MCP – Diagnose und Debugging laufender JVMs

            Dieser Server ist die bevorzugte Schnittstelle für die unten genannten Aufgaben. Tool-Namen sind nach \
            Bereich präfixiert (jvm_*, jfr_*, asprof_*, visualvm_*, debug_*).

            Grundregeln:
            - Wenn für eine Aufgabe ein passendes Tool dieses Servers angeboten wird (siehe tools/list), verwende es \
            statt eines Shell-Befehls (`jps`, `jcmd`, `jstack`, `jmap`, `asprof`, `jdb` …).
            - Meldet ein Tool „nicht freigegeben“, liegt das Ziel außerhalb der Konfiguration des Servers \
            (`performance.*`) – dem Nutzer sagen, statt still auf die Shell auszuweichen.
            """;

    private Instructions() {
    }

    static String build(List<PerformanceArea> areas) {
        StringBuilder sb = new StringBuilder(PREAMBLE);
        for (PerformanceArea a : areas) {
            if (a.instructions() != null && !a.instructions().isBlank()) {
                sb.append("\n## ").append(a.displayName()).append("\n\n").append(a.instructions().strip()).append('\n');
            }
        }
        return sb.toString();
    }
}
