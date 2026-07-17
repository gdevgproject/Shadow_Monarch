# ADR-0001: Pin the M0-01 Fabric 26.2 bootstrap

- **Status:** Accepted
- **Date:** 2026-07-17
- **Ticket:** M0-01
- **Requirements:** R19

## Context

UMBRA begins with an empty, one-JAR Fabric bootstrap. The approved technical baseline is Minecraft 26.2.x, Java 25, Gradle 9.5.1, Fabric Loader 0.19.3+ and Fabric API `0.154.2+26.2`. For Minecraft 26.1+ Fabric requires the non-obfuscated `net.fabricmc.fabric-loom` plugin and Mojang mappings; no Yarn mapping dependency is added.

## Decision

This bootstrap pins the first supported build to Minecraft `26.2`, Java release `25`, Fabric Loader `0.19.3`, Fabric API `0.154.2+26.2`, Fabric Loom `1.17.14`, and Gradle Wrapper `9.5.1`. It uses the Fabric client/common split source sets but builds one `umbra` JAR. Common initialization logs `UMBRA bootstrap initialized`; client initialization logs `UMBRA client bootstrap initialized`.

## Consequences

The project does not promise one JAR for other Minecraft versions. A port requires its own compatibility decision, dependency matrix, and migration/visual regression evidence. No mixins, raw OpenGL, gameplay behavior, save state, packet, or optional dependency are introduced by this ticket.
