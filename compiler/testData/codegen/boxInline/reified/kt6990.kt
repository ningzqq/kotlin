// FILE: 1.kt

package test

public inline fun <reified T : Any> inlineMeIfYouCan(): String? =
        {
            f {
                T::class.java.getName()
            }
        }()

inline fun f(x: () -> String) = x()

// FILE: 2.kt

import test.*

class OK

fun box(): String {
    return inlineMeIfYouCan<OK>()!!
}
