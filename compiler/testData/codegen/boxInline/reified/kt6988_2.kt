// FILE: 1.kt

package test


public inline fun <reified T: Any> Any.inlineMeIfYouCan() : () -> Runnable = {
    object : Runnable {
        override fun run() {
            T::class.java.newInstance()
        }
    }
}

// FILE: 2.kt

import test.*

fun box(): String {
    "yo".inlineMeIfYouCan<StringBuilder>()().run()
    return "OK"
}
