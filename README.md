# Student Task Planner

Student Task Planner is an Android app written in Java. The [PRD](prd.md) is a design specification and includes proposed features that are not all implemented. The functionality listed below reflects the current code.

## Current functionality

- Email and password sign-up and login.
- Create, edit, complete, and delete tasks, with a title, optional description, due date and time, priority, category, and subtasks.
- A home screen with the current date, task list, completion progress, and undo actions for task completion and deletion.
- A calendar screen for choosing a date and viewing its tasks.
- A statistics screen with task completion totals, a saved streak count, and a chart of completed tasks over the last seven days.
- Settings for light/system or dark appearance, notification preferences, manual synchronization, and logging out.
- Local task storage with Room and synchronization through Supabase.

## Requirements

- Android Studio and a JDK compatible with the project's Gradle setup.
- A Supabase project configured for the app.

## Configuration

Add the Supabase project URL and anon key to the root `local.properties` file:

```properties
supabase.url=YOUR_SUPABASE_URL
supabase.anonKey=YOUR_SUPABASE_ANON_KEY
```

The file is ignored by Git. Do not commit it. The Android Gradle plugin may also use `local.properties` for the local Android SDK path; keep any existing `sdk.dir` entry.

## Build and run

1. Open the project in Android Studio and allow Gradle to sync.
2. Run the app on an emulator or connected Android device.

Build a debug APK from Android Studio using **Build > Build Bundle(s) / APK(s) > Build APK(s)**. The APK is generated under `app/build/outputs/apk/debug/`.
