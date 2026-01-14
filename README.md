This is a Kotlin Multiplatform project targeting Android, iOS.

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./composeApp/src/jvmMain/kotlin)
    folder is the appropriate location.

* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE's toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

### Testing Deep Links

The app supports the `demo://` URL scheme for deep linking. The following deep link is available:

| Deep Link | Description |
|-----------|-------------|
| `demo://open-todo-view` | Opens the Add Todo screen |

#### Android Emulator

1. Make sure the app is installed and the emulator is running
2. Run the following command from terminal:
   ```bash
   adb shell am start -a android.intent.action.VIEW -d "demo://open-todo-view"
   ```

#### iOS Simulator

1. Make sure the app is installed and the simulator is running
2. Run the following command from terminal:
   ```bash
   xcrun simctl openurl booted "demo://open-todo-view"
   ```

   Alternatively, you can open Safari in the simulator and navigate to `demo://open-todo-view`.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…