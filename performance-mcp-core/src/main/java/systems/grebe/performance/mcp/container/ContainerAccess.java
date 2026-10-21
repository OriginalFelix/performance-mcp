package systems.grebe.performance.mcp.container;

import systems.grebe.container4j.ContainerRuntime;

/**
 * Schlanke Abstraktion auf Container4J für {@code container:<name>}-Ziele: welche Laufzeit verwendet wird und
 * welche Container als Ziel zulässig sind. Der Host kann sie selbst implementieren (devtools über sein
 * Container-Modul) oder {@link CliContainerAccess} verwenden.
 */
public interface ContainerAccess {

    /** Standard-Laufzeit oder {@code null}, wenn keine erreichbar ist. */
    ContainerRuntime runtimeOrNull();

    /** Standard-Laufzeit; wirft {@link IllegalStateException}, wenn keine erreichbar ist. */
    ContainerRuntime runtime();

    /** Wirft {@link IllegalArgumentException}, wenn der Containername ungültig oder nicht freigegeben ist. */
    void checkContainer(String container);

    boolean containerAllowed(String container);

    /** Keine Container-Ziele. */
    static ContainerAccess none() {
        return new CliContainerAccess(systems.grebe.performance.mcp.settings.ContainerSettings.disabled());
    }
}
