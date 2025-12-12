package dev.echoellet.dragonfist_legacy.platform.event

typealias EventCallback<T> = (T) -> Unit

interface EventHandler<T : Event> {
    fun register(callback: EventCallback<T>)
    fun invoke(event: T)

    companion object {
        fun <T : Event> createArrayBacked(): EventHandler<T> {
            return ArrayBackedEventHandler()
        }
    }
}
