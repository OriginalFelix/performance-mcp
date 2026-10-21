package systems.grebe.performance.mcp.server;

import java.util.List;

import org.springframework.ai.tool.ToolCallback;
import systems.grebe.performance.mcp.PerformanceArea;

/** Aktive Bereiche und ihre (präfixierten) Tool-Callbacks. */
public record PerformanceToolSet(List<PerformanceArea> areas, List<ToolCallback> tools) {
}
