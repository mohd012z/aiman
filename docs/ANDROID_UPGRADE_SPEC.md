# Aiman Android upgrade — implementation specification

Source baseline: `mohd012z/aimanage`. This specification is not evidence of completed code or an APK.

## P0: Navigation
- Replace 32-item always-visible drawer with grouped categories (Home, Optimize, Files, Apps, Network, Privacy, Settings); retain deep links for existing features.
- Provide consistent screen title, back navigation, scroll state, safe-area insets and accessible touch targets.
- Remove duplicated settings controls; distinguish live diagnostics from planned functionality.

## P1: App analyzer
- Display discoverable installed applications with names/icons and filter/search.
- Report usage history only after user grants Usage Access; never claim live per-app CPU measurement from usage stats.
- Recommend review/restriction with explicit rationale and confidence; exclude critical system, alarms, messaging, accessibility, navigation and active media apps by default.
- Open official app settings for user-controlled background restriction; no unsupported force-stop claims.

## P2: LOLA without GGUF
- Local Kotlin rules engine answers supported diagnostic questions offline.
- Optional HTTPS LOLA backend with authentication, timeout, explicit unavailable status and no embedded secrets.
- Tool outputs use structured evidence: measurement, timestamp, permission state, source, recommendation, uncertainty.
- No model hallucination of installed apps or measurements.

## P3: Cleaner
- Use SAF/MediaStore for user-selected content, SHA-256 duplicate detection and a reversible move plan.
- Preview each operation; require confirmation before deletion; never claim third-party private cache access.

## P4: Diagnostics
- Record thermal status, battery levels and supported device metrics with timestamps; avoid excessive sampling.
- Differentiate Wi-Fi link speed from actual measured internet throughput.
- Test offline, denied permissions, rotation, app restart and Android 16.

## CI acceptance
- Build debug APK and execute unit tests; compile instrumentation tests.
- Validate Android manifest permissions, security policies and release signing separately.
- Do not merge before required checks pass.
