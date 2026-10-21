package systems.grebe.performance.mcp.settings;

/**
 * Zugriff auf Container (Docker, Podman) für {@code container:<name>}-Ziele. Wird nur von der mitgelieferten
 * Implementierung {@link systems.grebe.performance.mcp.container.CliContainerAccess} verwendet; Hosts mit eigener
 * Container-Verwaltung implementieren stattdessen {@link systems.grebe.performance.mcp.container.ContainerAccess}.
 *
 * @param enabled           {@code false} = keine Container-Ziele
 * @param runtime           {@code auto} (erste erreichbare), {@code docker} oder {@code podman}
 * @param dockerBinary      Pfad zur docker-CLI oder Name im PATH
 * @param dockerContext     optional {@code --context}
 * @param dockerHost        optional {@code -H}, z.B. {@code tcp://build01:2375}
 * @param podmanBinary      Pfad zur podman-CLI oder Name im PATH
 * @param podmanConnection  optional {@code --connection}
 * @param podmanUrl         optional {@code --url}
 * @param allowedContainers Regulärer Ausdruck auf Containernamen, die als Ziele zulässig sind
 */
public record ContainerSettings(boolean enabled, String runtime, String dockerBinary, String dockerContext,
                                String dockerHost, String podmanBinary, String podmanConnection, String podmanUrl,
                                String allowedContainers) {

    public ContainerSettings {
        runtime = runtime == null || runtime.isBlank() ? "auto" : runtime.trim().toLowerCase(java.util.Locale.ROOT);
        dockerBinary = dockerBinary == null || dockerBinary.isBlank() ? "docker" : dockerBinary;
        podmanBinary = podmanBinary == null || podmanBinary.isBlank() ? "podman" : podmanBinary;
        dockerContext = JavaSettings.blankToNull(dockerContext);
        dockerHost = JavaSettings.blankToNull(dockerHost);
        podmanConnection = JavaSettings.blankToNull(podmanConnection);
        podmanUrl = JavaSettings.blankToNull(podmanUrl);
        allowedContainers = allowedContainers == null || allowedContainers.isBlank() ? ".*" : allowedContainers;
    }

    public static ContainerSettings defaults() {
        return new ContainerSettings(true, "auto", null, null, null, null, null, null, ".*");
    }

    public static ContainerSettings disabled() {
        return new ContainerSettings(false, "auto", null, null, null, null, null, null, ".*");
    }
}
