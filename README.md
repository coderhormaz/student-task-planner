# Student Task Planner

An Android task planner for students, written in Java with a Material You 3 design. The feature list below comes from the project's PRD (`prd.md`) — where it says "planned", that's the spec; it hasn't all been verified as implemented yet.

## Features (planned, per PRD)

The PRD in `prd.md` is the source of truth for what's planned. In outline, it's a student-focused planner:

- Create, organise and track study tasks and assignments
- Material You 3 interface — dynamic colour and modern Android components

For the full planned feature set, user flows and requirements, see `prd.md` in this repository.

## Tech Stack

- Android, Java
- Material You 3 (Material Design 3 components)
- Gradle

## Getting Started

Prerequisites: Android Studio and a JDK compatible with the project's Gradle setup.

1. Open the project in Android Studio and let Gradle sync.
2. Run it on an emulator or a connected device.

Or build a debug APK from the command line:

```bash
./gradlew assembleDebug
```

The APK is generated under `app/build/outputs/apk/debug/`.

## Notes

- Status: features above are described from the PRD, not from a verified run of the app — check `prd.md` against the code before relying on any individual feature being finished.
