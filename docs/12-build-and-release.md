# Build and release guide

This document covers how to build PrintXpress in Android Studio, generate a debug APK for testing, sign and generate a release APK, and publish a GitHub release.

---

## Prerequisites

- **Android Studio** (a recent stable version) with the Android SDK for API 36. Download from [developer.android.com/studio](https://developer.android.com/studio).
- **Java 17 or later**, which Android Studio bundles automatically.
- **Android SDK Build-Tools**, installed through the SDK Manager in Android Studio.

---

## Open the project

1. Clone the repository.

   ```
   git clone https://github.com/TharushiHiranya/PrintXpress.git
   ```

2. Open the cloned folder in Android Studio. Wait for the Gradle sync to finish. A notification at the bottom of the screen confirms sync is complete.

---

## Debug build

A debug build is unsigned and meant for development and testing.

### From Android Studio

1. Connect an Android phone with USB debugging on, or start an emulator.
2. Press **Run** (Shift + F10) or click the green play button. Android Studio builds the APK, installs it, and launches the app.

### From the command line

Open a terminal in the project root and run:

```
gradlew assembleDebug
```

On macOS or Linux use `./gradlew assembleDebug`.

The APK is saved to:

```
app/build/outputs/apk/debug/app-debug.apk
```

---

## Release build

A release build is optimised and must be signed with a keystore before it can be installed.

### Step 1 — create a keystore

You only need to do this once. Keep the keystore file and its passwords safe. Losing them means you cannot update the app on the same key in future.

1. In Android Studio, open **Build > Generate Signed Bundle / APK**.
2. Choose **APK** and click **Next**.
3. Click **Create new...** next to the Key store path field.
4. Fill in the path where you want to save the `.jks` file, a key store password, a key alias, a key password, and your name or organisation. Click **OK**.

### Step 2 — configure signing in the build file

Open [`app/build.gradle.kts`](../app/build.gradle.kts) and add a `signingConfigs` block, then reference it in the `release` build type.

```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("path/to/your.jks")
            storePassword = "your_keystore_password"
            keyAlias = "your_key_alias"
            keyPassword = "your_key_password"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

Do not commit the keystore or the passwords to the repository. Move the credentials to a local `keystore.properties` file (add it to `.gitignore`) and read them from there if others will contribute to the project.

### Step 3 — build the release APK

```
gradlew assembleRelease
```

The signed APK is saved to:

```
app/build/outputs/apk/release/app-release.apk
```

Alternatively, finish the **Generate Signed Bundle / APK** wizard in Android Studio: choose your keystore, select the `release` build variant, and click **Finish**.

---

## Publish a GitHub release

1. Go to the repository on GitHub and click **Releases** in the right sidebar.
2. Click **Draft a new release**.
3. Click **Choose a tag** and type a version number such as `v1.0.0`. Click **Create new tag**.
4. Set the release title, for example `PrintXpress v1.0.0`.
5. Add a short description of what is in the release.
6. Drag the signed `app-release.apk` file into the assets section, or click **Attach binaries** to browse for it.
7. Click **Publish release**.

The APK is now available for download from the Releases page and the download badge in the README will update automatically.
