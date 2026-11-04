package systems.grebe.performance.mcp.asprof;

import org.junit.jupiter.api.Test;
import systems.grebe.performance.mcp.java.StackProfile.Kind;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AsyncProfilerEventsTest {

    @Test
    void methodEventsAreRecognized() {
        assertThat(AsyncProfilerTools.kind(null)).isEqualTo(Kind.CPU);
        assertThat(AsyncProfilerTools.kind("lock")).isEqualTo(Kind.LOCK);
        assertThat(AsyncProfilerTools.kind("com.acme.Dao.find*")).isEqualTo(Kind.METHOD);
        assertThat(AsyncProfilerTools.kind("java.util.ArrayList.<init>")).isEqualTo(Kind.METHOD);
        assertThat(AsyncProfilerTools.kind("malloc")).isEqualTo(Kind.METHOD);
        // nie als asprof-Option durchreichen
        assertThatThrownBy(() -> AsyncProfilerTools.kind("-f /etc/passwd")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> AsyncProfilerTools.kind("a b")).isInstanceOf(IllegalArgumentException.class);
    }
}
