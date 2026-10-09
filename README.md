# Aiman — Android Device Intelligence

This repository is the intended successor to [Almanage](https://github.com/mohd012z/aimanage).

## Migration status

**Source migration pending.** Do not interpret this README as an APK build or a source-code transfer. The existing application remains in the original repository until its full Android project and Git history can be copied and validated.

## Migration acceptance criteria

1. Import the complete Android project from `mohd012z/aimanage`, preserving Gradle wrapper, source, tests, resources, and workflows.
2. Verify the Gradle debug build, unit tests, instrumentation compile, manifest permissions, and release checks.
3. Keep the original repository unchanged until the new build is green.
4. Implement upgrades incrementally via PRs: UI/navigation; app discovery and permissions; hybrid LOLA assistant without GGUF; safe storage analysis and organizer; thermal/battery/network histories; security and sleep diagnostics.
5. Require user confirmation for destructive file operations and for changing app settings. Android does not allow ordinary apps to silently force-stop arbitrary third-party apps or clear their private caches.

## AI architecture

Local Kotlin diagnostic rules provide offline guidance; a separately configured authenticated LOLA service provides optional natural-language reasoning. Never expose API credentials in the APK, fabricate readings, or claim that a model is connected when it is not.
