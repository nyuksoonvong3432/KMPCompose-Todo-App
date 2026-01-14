package com.habitap.todoapp.navigation

/**
 * Singleton object for handling deep links from external sources.
 *
 * This handler manages incoming URIs from the operating system and notifies
 * registered listeners. It caches URIs received before a listener is registered
 * to ensure deep links are not lost during app initialization.
 *
 * Platform-specific code should call [onNewUri] when the app receives a deep link.
 * The main app composable should set the [listener] to handle navigation.
 */
object ExternalUriHandler {
    /**
     * Cached URI received before a listener was registered.
     * Automatically cleared once delivered to a listener.
     */
    private var cached: String? = null

    /**
     * Listener callback invoked when a new URI is received.
     * Setting this property will immediately deliver any cached URI.
     */
    var listener: ((uri: String) -> Unit)? = null
        set(value) {
            field = value
            if (value != null) {
                cached?.let { value.invoke(it) }
                cached = null
            }
        }

    /**
     * Called by platform-specific code when a new deep link URI is received.
     *
     * If a listener is registered, the URI is delivered immediately.
     * Otherwise, it is cached until a listener is set.
     *
     * @param uri The deep link URI string
     */
    fun onNewUri(uri: String) {
        cached = uri
        listener?.let {
            it.invoke(uri)
            cached = null
        }
    }
}
