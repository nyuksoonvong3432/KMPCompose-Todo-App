import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
                .onOpenURL { url in
                    // Handle incoming deep link URLs
                    ExternalUriHandler.shared.onNewUri(uri: url.absoluteString)
                }
        }
    }
}