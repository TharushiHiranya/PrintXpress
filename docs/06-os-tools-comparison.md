# Mobile operating systems, tools, and technologies

This section compares the main mobile operating systems, the tools used to build apps for them, and the core technologies, then explains the choices made for PrintXpress. It covers Task A of the assignment.

## Why the platform choice matters

The platform decides who can install the app, what tools are needed, and how much the project costs to build and run. For a printing service aimed at customers across Sri Lanka, reach and cost matter more than anything else, so the comparison below keeps the local market in view throughout.

## Mobile operating systems

Two operating systems hold almost the entire mobile market. Android, made by Google, runs on phones from many makers such as Samsung, Xiaomi, and Google itself. iOS, made by Apple, runs only on the iPhone. Together they cover more than 99 percent of devices in use, so any other system can be set aside for this project (StatCounter, 2026).

The split between the two is not even. Worldwide, Android holds about 70 percent of the mobile market and iOS about 29 percent (StatCounter, 2026). The gap is far wider in Sri Lanka, where Android held roughly 87 percent of the market against about 12 percent for iOS (Statista, 2024). For a Sri Lankan customer base this is the single most important fact in the whole comparison. An Android app reaches close to nine in ten local users, while an iOS app would reach barely one in ten.

The two systems also differ in how apps are distributed and how open they are. Android allows installation from the Google Play Store and from other sources, and the wider range of device prices means it dominates in markets where budget phones are common (StatCounter, 2026). iOS is a closed system tied to Apple hardware, which gives a consistent experience and strong privacy controls but a smaller and more expensive device range. Apple users tend to spend more on apps, which matters for paid apps and in-app purchases, but PrintXpress earns money from print orders rather than app sales, so that advantage does not apply here.

## Development tools

Each platform has its own official tools, and they are not interchangeable.

Android apps are built in Android Studio, the official environment from Google, which is free and runs on Windows, macOS, and Linux (Google, 2025a). It includes the code editor, the build system based on Gradle, a layout preview, a device emulator, and a profiler. Because it runs on Windows and Linux as well as macOS, a student can build Android apps on almost any computer.

iOS apps are built in Xcode, Apple's environment, which is free but runs only on macOS (Apple, 2025a). This is a hard limit for many students in Sri Lanka, since it means owning a Mac. The need for Apple hardware to build, test, and publish an iOS app raises the real cost of the platform well beyond the device share figures alone.

This difference is decisive for the project. Android Studio runs on the hardware the student already has, while Xcode would require buying a Mac. For a single-student university project, that settles the tooling question in favour of Android.

## Languages and UI technologies

The native languages are Kotlin for Android and Swift for iOS. Both are modern, type-safe languages with similar features such as null safety and concise syntax. Kotlin is the language Google recommends first for Android, and most new Android code and documentation now use it (Google, 2025b). Swift plays the same role for Apple platforms (Apple, 2025b). For a beginner, both are reasonable, so the language is not the deciding factor, the platform is.

For building screens, Android offers Jetpack Compose and iOS offers SwiftUI. Both are declarative UI toolkits, which means the developer describes what the screen should look like for a given state and the toolkit redraws it when the state changes. Compose replaces the older XML layout approach on Android and leads to less code and fewer files (Google, 2025c). For PrintXpress, Compose is used for the whole interface, with no XML layouts, which keeps the UI in one language and one place.

## Native versus cross-platform

A third option exists. Cross-platform frameworks such as Flutter, React Native, and Ionic let one codebase target both Android and iOS. Their appeal is clear, since one team can ship to both stores. They also bring trade-offs, including an extra layer between the app and the device, slower access to the newest platform features, and a larger app size.

This option is not available for the assignment. The brief states that cross-platform frameworks are not permitted and that all development must be native (CSE5011 assessment brief, 2026). Even without that rule, native Android would still be the sensible pick here, because the audience is overwhelmingly on Android, the project is small, and a single native codebase is simpler to learn and to debug than a framework with its own runtime.

## Storage technology

PrintXpress stores all data on the device with Room, which sits on top of SQLite, the database engine built into Android (Google, 2025d). This is the standard local storage choice for native Android and needs no server, so the app works offline and costs nothing to run. The alternative, a cloud database such as Firebase, would add real accounts and sync across devices but would also need internet, a Google project, and ongoing setup. For an offline-first ordering app built by one student, local storage is the simpler and more reliable choice, and it keeps the whole database in a single portable file.

## Summary of the comparison

| Factor | Android (chosen) | iOS |
|--------|------------------|-----|
| Local market share, Sri Lanka | About 87 percent (Statista, 2024) | About 12 percent (Statista, 2024) |
| Global market share | About 70 percent (StatCounter, 2026) | About 29 percent (StatCounter, 2026) |
| Build tool | Android Studio, runs on Windows, macOS, Linux | Xcode, macOS only |
| Hardware needed to build | Any laptop | A Mac |
| Language | Kotlin | Swift |
| UI toolkit | Jetpack Compose | SwiftUI |
| Cost to start | Free | Free software, but Mac required |

## Decision

PrintXpress is built as a native Android app with Kotlin, Jetpack Compose, and Room over SQLite. The reasoning is direct. The target market is almost entirely on Android, Android Studio runs on the student's existing hardware, Kotlin and Compose are the current recommended native stack, and local storage keeps the app offline, cheap, and portable. The assignment also requires native development, which rules cross-platform frameworks out. Together these reasons make native Android the right fit for both the users and the scope of this project.

## References

Apple (2025a) *Xcode*. Available at: https://developer.apple.com/xcode/ (Accessed: 24 May 2026).

Apple (2025b) *Swift*. Available at: https://developer.apple.com/swift/ (Accessed: 24 May 2026).

Google (2025a) *Meet Android Studio*. Available at: https://developer.android.com/studio/intro (Accessed: 24 May 2026).

Google (2025b) *Kotlin and Android*. Available at: https://developer.android.com/kotlin (Accessed: 24 May 2026).

Google (2025c) *Jetpack Compose*. Available at: https://developer.android.com/jetpack/compose (Accessed: 24 May 2026).

Google (2025d) *Save data in a local database using Room*. Available at: https://developer.android.com/training/data-storage/room (Accessed: 24 May 2026).

StatCounter (2026) *Mobile operating system market share worldwide*. Available at: https://gs.statcounter.com/os-market-share/mobile/worldwide (Accessed: 24 May 2026).

Statista (2024) *Sri Lanka: monthly mobile operating system market share*. Available at: https://www.statista.com/statistics/931178/sri-lanka-mobile-os-share/ (Accessed: 24 May 2026).
