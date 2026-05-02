# Aura: Agentic Meeting Intelligence

Built at the **Build with AI: App Roadshows** event with AntiGravity.

Aura is an on-device Android app that turns meeting recordings into structured intelligence — decisions, action items, and personalized follow-up drafts — powered by the Gemini API.

## What it does

1. **Record** a meeting with one tap
2. **Analyse** — a multi-stage Gemini pipeline extracts:
   - 2–3 sentence summary
   - Key decisions made
   - Action items with owner, deadline, priority, and confidence score
3. **Draft** — auto-composes a personalized follow-up message per assignee
4. **Dispatch** — send drafts via any app using Android's share sheet

## Stack

- Kotlin + Jetpack Compose
- Room (local DB) + Hilt (DI)
- Gemini API via REST (`gemini-2.0-flash`)
- Google Secrets Gradle Plugin for API key injection

## Setup

1. Get a Gemini API key from [Google AI Studio](https://aistudio.google.com)
2. Create `local.properties` in the project root (already gitignored):
   ```
   GEMINI_API_KEY=your_key_here
   ```
3. Build and run on Android API 26+

`local.properties` is never committed — the key is injected at build time via `BuildConfig` using the [Google Secrets Gradle Plugin](https://github.com/google/secrets-gradle-plugin).
