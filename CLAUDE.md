# CLAUDE.md

This file guides any work done in the PrintXpress project. Read it before doing anything. If anything here conflicts with a one-off instruction in chat, ask first.

## What PrintXpress is

PrintXpress is a native Android app for a digital printing service in Sri Lanka. Customers browse print products (business cards, posters, banners, flyers, stickers, mugs, and t-shirts), upload a design or type custom text, place an order for pickup or delivery, and track it. The app also shows sample designs, print guidelines, FAQs, and seasonal offers.

This is a university assignment for one student with beginner coding knowledge. The goal is a clean, working app that a marker can read and understand, not a production system.

## Who you are writing for

The student is new to coding. Always pick the simplest approach that works. No clever tricks, no advanced patterns when a basic one does the job. Code should be easy to read out loud and explain in a viva.

## Tech stack

- Language: Kotlin.
- UI: Jetpack Compose only. No XML layouts.
- State: ViewModel plus `remember` and `mutableStateOf`. Keep it simple.
- Local storage: Room, which sits on top of SQLite.
- Build: Gradle with the version catalog in `gradle/libs.versions.toml`.
- Package: `com.hiranya.printxpress`.
- minSdk 24, targetSdk 36.

Cross-platform frameworks are not allowed. No Flutter, no React Native, no Ionic. Native Android only. This is an assignment rule, not a preference.

## Hard rules

1. Ask before adding any library, dependency, or plugin. This includes Room. When you need one, say which one, why, and the exact version, then wait.
2. Native Android only. Never reach for a cross-platform tool.
3. Simplest solution first. If two approaches both work, use the shorter one.
4. Add a short comment above each block that says what it does and why. Keep comments practical.
5. Do not invent product features that are not in the assignment or the docs. Check `docs/` first.

## Colours and theme

The brand is one accent colour on a white background with dark gray text. Define these once in `ui/theme/Color.kt` and use the names everywhere. Do not hardcode hex values inside screens.

| Role | Hex | Notes |
|------|-----|-------|
| Accent (primary) | `#A20BC8` | Buttons, links, highlights, selected states. |
| Accent container | `#F6E7FA` | Light accent fill for chips, cards, and selected backgrounds. |
| On accent | `#FFFFFF` | Text and icons on top of the accent colour. |
| Background | `#FFFFFF` | App background and surfaces. |
| Text primary | `#1A1A1A` | Headings and body text. |
| Text secondary | `#5C5C5C` | Captions, hints, and supporting text. |
| Text disabled | `#9A9A9A` | Disabled labels and placeholders. |
| Divider | `#E0E0E0` | Lines, borders, and dividers. |

Use the Material 3 light colour scheme. Map `primary` to the accent, `onPrimary` to white, `primaryContainer` to the light accent, `background` and `surface` to white, and `onBackground` and `onSurface` to text primary. Keep dark theme off for now to keep things simple, unless asked.

## Database

Use Room over SQLite for all local data. The whole database lives on the device, so the app works offline and needs no backend. This is the simplest reliable option, and it is portable because the data file travels with the app.

How to keep it portable and easy to share with the project:

1. Define the tables as Room entities (see `docs/04-database-design.md` for the schema).
2. Seed the starting catalog (categories, products, and sample promotions) the first time the database is created, using a `RoomDatabase.Callback`. This avoids file format mismatches and is the most reliable choice for a beginner.
3. For a shippable copy of the data, export the SQLite file once it is built. It can be opened with DB Browser for SQLite (free) so a marker can inspect the tables. If a pre-filled file is preferred, place it in `app/src/main/assets/database/` and load it with Room's `createFromAsset`. Pick one approach, do not mix both.

Never store plain text passwords. Hash them before saving. Use email or phone as the login id.

## Project structure

Keep the code grouped by purpose so it is easy to follow.

```
app/src/main/java/com/hiranya/printxpress/
  data/        Room entities, DAOs, the database class, and repositories
  ui/
    theme/     Color.kt, Type.kt, Theme.kt
    screens/   one file per screen (Compose)
    components/ small reusable Composables
  viewmodel/   one ViewModel per screen area
  MainActivity.kt
docs/          all documentation and diagrams (see below)
```

## Writing rules

These apply to every word that ships in the repo: code comments, documentation, UI text, and commit messages. They keep the writing clear and human.

- Write like a knowledgeable friend, not a manual. Be direct and useful.
- No em dashes. Use commas, short sentences, or brackets instead.
- No semicolons in prose. Break into two sentences.
- Use the Oxford comma.
- Sentence case for all headings, labels, and buttons.
- Say "use" not "utilize." Avoid "leverage," "facilitate," "ensure," and "streamline."
- No filler openers like "certainly," "absolutely," or "of course."
- Lead with the answer. Keep paragraphs to 2 to 4 sentences.
- Numbered steps for step-by-step tasks. Prose for everything else.
- Bold only what genuinely needs emphasis.
- Code always goes in code blocks.

## Sounding like the student's own work

This is graded work, so everything in the repo must read as one student's own writing.

- Write in a plain, natural human voice. No AI tells, no hedging, no padded intros.
- Do not mention Claude, AI, assistants, prompts, or generated content anywhere in the code, comments, docs, or commit history.
- No decorative banners, no "auto-generated" notes, no long boilerplate headers on files.
- Comments are short and specific, the kind a developer writes for themselves.
- This CLAUDE.md file is the one exception. It is a working note and will be removed before submission. Do not reference it from any other file.

## Commit messages

Do not run `git commit`. The student commits manually.

After each change, post a short list of single-line commit message options in chat so the student can pick one. Use the Conventional Commits format.

```
type(scope): short description in the imperative
```

- Types: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`, `build`.
- Scope is optional and lowercase, for example `feat(order):` or `docs(er):`.
- One line only. No body, no footer. Lowercase start, no full stop at the end.
- Give 3 to 5 options that describe the same change in different words, then stop.

Example after adding the order screen:

```
feat(order): add order placement screen with specs and quantity
feat(order): build place order flow with pickup and delivery options
feat: let customers select specs and place a print order
```

## docs/ directory

All documentation lives in `docs/`. Treat it as the source of truth for scope and design.

- `01-project-overview.md` scope, goals, and the actors.
- `02-user-stories.md` user stories with acceptance criteria.
- `03-requirements.md` functional and non-functional requirements.
- `04-database-design.md` Room schema, tables, keys, and the portable approach.
- `05-diagrams.md` explanations and design decisions for every diagram.
- `06-os-tools-comparison.md` Task A, the mobile OS, tools, and technology comparison.
- `07-test-plan.md` Task E, the test plan with cases and data.
- `08-user-documentation.md` Task F, the user guide.
- `09-technical-documentation.md` Task F, the technical guide.
- `diagrams/` the draw.io source files (`.drawio`).

Diagram style for every `.drawio` file: accent `#A20BC8` for borders, text, arrows, and lines, light `#F6E7FA` for shape fills, right-angle (orthogonal) connectors with no curves, and no unrelated lines crossing or overlapping.
