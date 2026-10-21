package systems.grebe.performance.mcp.server;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import io.modelcontextprotocol.server.McpServerFeatures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/** Kontext startet, alle 34 Tools (inkl. invasiver) sind als MCP-Tools registriert, Instructions sind gesetzt. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "performance.home=${java.io.tmpdir}/performance-mcp-test-home")
class PerformanceMcpApplicationTest {

    @Autowired
    PerformanceToolSet tools;

    @Autowired
    List<McpServerFeatures.SyncToolSpecification> specs;

    @Test
    void registersAllToolsWithoutPermissionLogic() throws IOException {
        Set<String> expected = new TreeSet<>(List.of(new String(getClass().getResourceAsStream("/expected-tools.txt")
                .readAllBytes()).trim().split("\\s+")));
        Set<String> actual = new TreeSet<>();
        specs.forEach(s -> actual.add(s.tool().name()));
        assertThat(actual).isEqualTo(expected);
        assertThat(tools.areas()).hasSize(5);
        assertThat(specs).filteredOn(s -> s.tool().name().equals("jvm_info"))
                .singleElement().satisfies(s -> assertThat(s.tool().annotations().readOnlyHint()).isTrue());
    }

    @Test
    void instructionsMentionEveryArea() {
        String text = Instructions.build(tools.areas());
        assertThat(text).contains("jvm_processes", "jfr_record", "asprof_profile", "visualvm_heap_analyze", "debug_attach");
    }
}
