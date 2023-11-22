import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

/**
 * data class toMap()
 */
fun <T : Any> T.toMap(): Map<String, Any?> {
    return (this::class as KClass<T>).memberProperties.associate { prop ->
        prop.name to prop.get(this)?.let { value ->
            if (value::class.isData) {
                value.toMap()
            } else {
                value
            }
        }
    }
}