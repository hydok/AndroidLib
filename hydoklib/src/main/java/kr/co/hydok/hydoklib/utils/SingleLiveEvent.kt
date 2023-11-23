package kr.co.hydok.hydoklib.utils

import android.os.Looper
import androidx.annotation.MainThread
import androidx.core.util.Consumer
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean
//또르르2
class SingleLiveEvent<T> : MutableLiveData<T>(), Consumer<T>, (T) -> Unit {
    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            //"with: Multiple observers registered but only one will be notified of changes."
        }
        // Observe the internal MutableLiveData
        super.observe(owner) { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }

    @MainThread
    override fun setValue(value: T?) {
        pending.set(true)
        super.setValue(value)
    }

    override fun postValue(value: T) {
        pending.set(true)
        super.postValue(value)
    }

    fun setValueSafety(value: T) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            //"setValueSafety main"
            setValue(value)
        } else {
            //"setValueSafety not main"
            postValue(value)
        }
    }

    @MainThread
    fun call() {
        value = null
    }

    override fun accept(value: T) {
        setValueSafety(value)
    }

    override fun invoke(value: T) {
        setValueSafety(value)
    }
}