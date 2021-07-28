package phoenix.other

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import phoenix.init.PhxConfiguration

//TODO make fast logs for render thread by RenderSystem.isOnRenderThread()
val LOGGER = LogManager.getLogger("PhoenixProject")!!

inline fun<reified T> T.debug(message : String)
{
    if (PhxConfiguration.isDebugMode)
        LOGGER.error("<${T::class.java.lastName()}> " + message)
    else
        LOGGER.log(Level.DEBUG, "<${T::class.java.lastName()}> " + message)
}

inline fun<reified T> T.error(message : String?) = error(T::class.java.lastName(), message)
inline fun<reified T> T.error(exception : Exception?)
{
    if (exception != null)
    {
        val builder = StringBuilder("Exception in class <${T::class.java.lastName()}>: ${exception.message} at ${traceToString(exception.stackTrace[0])}")

        val cause = exception.cause
        if (cause != null)
            builder.append(" because: ${cause.message} at ${traceToString(cause.stackTrace[0])}")

        LOGGER.error(builder)
    }
}

fun debug(from : String, message : String?) = LOGGER.debug("<$from> $message")
fun error(from : String, message : String?) = LOGGER.error("<$from> $message")
fun error(from : String, exception : Exception?)
{
    if (exception != null)
    {
        val builder =
            StringBuilder("Exception in class <$from>: ${exception.message} at ${traceToString(exception.stackTrace[0])}")

        val cause = exception.cause
        if (cause != null)
            builder.append(" because: ${cause.message} at ${traceToString(cause.stackTrace[0])}")

        LOGGER.error(builder)
    }
}

fun traceToString(element : StackTraceElement) : String
{
    val builder = StringBuilder()
    if (element.fileName?.split(".")?.get(0) != element.className.split(".").last())
        builder.append(element.fileName!!.split(".")[0])
    else
        builder.append(element.className)
    builder.append("::${element.methodName}")
    return builder.toString()
}

fun<T> Class<T>.lastName() = this.canonicalName.split(".").last()