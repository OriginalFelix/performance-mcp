plugins {
    id("org.springframework.boot")
}

// Eigenständiger MCP-Server für die Performance- und Debug-Tools: Streamable HTTP (Standard) oder stdio (Profil "stdio").
dependencies {
    implementation(project(":performance-mcp-core"))

    implementation("org.springframework.ai:spring-ai-starter-mcp-server-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-jetty")
}

springBoot {
    mainClass = "systems.grebe.performance.mcp.server.PerformanceMcpApplication"
}

tasks.named<Jar>("bootJar") {
    archiveBaseName = "performance-mcp"
}
