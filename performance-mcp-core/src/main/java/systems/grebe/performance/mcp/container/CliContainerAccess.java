package systems.grebe.performance.mcp.container;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import systems.grebe.container4j.ContainerRuntime;
import systems.grebe.container4j.cli.DockerRuntime;
import systems.grebe.container4j.cli.PodmanRuntime;
import systems.grebe.performance.mcp.settings.ContainerSettings;

/** {@link ContainerAccess} über die Docker-/Podman-Kommandozeile (Container4J). */
public final class CliContainerAccess implements ContainerAccess {

    private static final Duration PROBE_TTL = Duration.ofSeconds(30);
    private static final Pattern NAME = Pattern.compile("[A-Za-z0-9][A-Za-z0-9_.-]*");

    private record Probe(ContainerRuntime.Availability availability, long at) { }

    private final Map<String, ContainerRuntime> runtimes = new LinkedHashMap<>();
    private final Map<String, Probe> probes = new ConcurrentHashMap<>();
    private final String wanted;
    private final Pattern allowed;

    public CliContainerAccess(ContainerSettings s) {
        if (s.enabled()) {
            if (!"podman".equals(s.runtime())) {
                runtimes.put(DockerRuntime.ID, new DockerRuntime(s.dockerBinary(), s.dockerContext(), s.dockerHost()));
            }
            if (!"docker".equals(s.runtime())) {
                runtimes.put(PodmanRuntime.ID, new PodmanRuntime(s.podmanBinary(), s.podmanConnection(), s.podmanUrl()));
            }
        }
        this.wanted = s.runtime();
        try {
            this.allowed = Pattern.compile(s.allowedContainers());
        } catch (PatternSyntaxException e) {
            throw new IllegalStateException("Ungültiger regulärer Ausdruck für erlaubte Container: " + s.allowedContainers());
        }
    }

    @Override
    public ContainerRuntime runtime() {
        if (runtimes.isEmpty()) {
            throw new IllegalStateException("Keine Container-Laufzeit aktiviert.");
        }
        if (!"auto".equals(wanted)) {
            return runtimes.get(wanted);
        }
        List<String> tried = new java.util.ArrayList<>();
        for (var e : runtimes.entrySet()) {
            ContainerRuntime.Availability a = availability(e.getKey(), e.getValue());
            if (a.available()) {
                return e.getValue();
            }
            tried.add(e.getKey() + ": " + a.message());
        }
        throw new IllegalStateException("Keine Container-Laufzeit erreichbar: " + String.join("; ", tried));
    }

    @Override
    public ContainerRuntime runtimeOrNull() {
        try {
            return runtime();
        } catch (RuntimeException e) {
            return null;
        }
    }

    private ContainerRuntime.Availability availability(String id, ContainerRuntime rt) {
        Probe p = probes.get(id);
        long now = System.currentTimeMillis();
        if (p == null || now - p.at() > PROBE_TTL.toMillis()) {
            p = new Probe(rt.probe(), now);
            probes.put(id, p);
        }
        return p.availability();
    }

    @Override
    public void checkContainer(String container) {
        if (container == null || !NAME.matcher(container).matches()) {
            throw new IllegalArgumentException("Ungültiger Containername: " + container);
        }
        if (!allowed.matcher(container).matches()) {
            throw new IllegalArgumentException("Container '" + container + "' ist nicht freigegeben (performance.container.allowed-containers).");
        }
    }

    @Override
    public boolean containerAllowed(String container) {
        return allowed.matcher(container).matches();
    }
}
