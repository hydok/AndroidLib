package kr.co.hydok.hydoklib.utils

import androidx.annotation.Keep
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

object EventBus : CoroutineScope {

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext = Dispatchers.IO + job

    private val contexts = mutableMapOf<Any, MutableMap<KClass<*>, EventData<*>>>()

    fun send(event: Any) {
        for ((_, data) in contexts) {
            data.keys
                .firstOrNull { it == event::class }
                .let { key ->
                    data[key]?.send(event)
                }
        }
    }

    fun <T : Any> register(
        lifecycle: LifecycleOwner,
        eventClass: KClass<T>,
        eventDispatcher: CoroutineDispatcher = Dispatchers.Main,
        eventCallback: (T) -> Unit
    ) {
        val datas = getEventDatas(lifecycle)
        val eventData = EventData(this, eventDispatcher, eventCallback)
        datas[eventClass] = eventData
    }

    private fun getEventDatas(lifecycle: Any): MutableMap<KClass<*>, EventData<*>> {
        var datas = contexts[lifecycle]
        if (datas == null) {
            datas = mutableMapOf()
            contexts[lifecycle] = datas
        }

        return datas
    }

    fun unregisterAllEvent() {
        coroutineContext.cancelChildren()
        contexts.clear()
    }

    fun unregister(lifecycle: Any) {
        contexts.remove(lifecycle)
    }
}

@Keep
data class EventData<T>(
    val coroutineScope: CoroutineScope,
    val eventDispatcher: CoroutineDispatcher,
    val onEvent: (T) -> Unit
) {

    private val flow = MutableSharedFlow<T>()

    init {
        coroutineScope.launch {
            flow.collect { launch(eventDispatcher) { onEvent(it) } }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun send(event: Any) {
        coroutineScope.launch {
            yield()
            flow.emit(event as T)
        }
    }
}
