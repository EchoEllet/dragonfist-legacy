package dev.echoellet.dragonfist_legacy.platform.event

class ArrayBackedEventHandler<T : Event> : EventHandler<T> {
    private val callbacks = mutableListOf<EventCallback<T>>()

    override fun register(callback: EventCallback<T>) {
        callbacks.add(callback)
    }

    override fun invoke(event: T) {
        for (callback in callbacks) {
            callback(event)
        }
    }
}
