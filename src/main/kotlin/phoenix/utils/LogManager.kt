package phoenix.utils

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import phoenix.init.PhoenixConfiguration

object LogManager
{
    private val LOGGER = LogManager.getLogger()!!
    @JvmStatic
    fun log(obj : Any, message : String)
    {
        if(PhoenixConfiguration.COMMON_CONFIG.debug.get())
            LOGGER.error("<${obj.javaClass.lastName()}> " + message)
        else
            LOGGER.log(Level.DEBUG, "<${obj.javaClass.lastName()}> " + message)
    }

    @JvmStatic
    fun log(from : String, message : String)
    {
        if(PhoenixConfiguration.COMMON_CONFIG.debug.get())
            LOGGER.error("<$from> $message")
        else
            LOGGER.log(Level.DEBUG, "<$from> $message")
    }

    @JvmStatic
    fun error(obj : Any, message : String?) = LOGGER.error("<${obj.javaClass.lastName()}> " + (message ?: ""))

    @JvmStatic
    fun error(obj : Any, message : Exception?)
    {
        if(message != null)
            LOGGER.error("Exception in class <${obj.javaClass.lastName()}>: " + message.toString())
    }

    @JvmStatic
    fun error(from : String, message : Exception?)
    {
        if(message != null)
            LOGGER.error("Exception in class <$from>: $message")
    }

    @JvmStatic
    fun error(from : String, message : String) = LOGGER.error("<$from> $message")

    @JvmStatic
    fun errorObjects(obj : Any, vararg objects : Any)
    {
        var message = ""
        for (i in objects)
            message += " $i"
        error(obj, message)
    }

    @JvmStatic
    fun errorObjects(from : String, vararg objects : Any)
    {
        var message = ""
        for (i in objects)
            message += " $i"
        error(from, message)
    }

    private fun<T> Class<T>.lastName() = this.canonicalName.split(".").last()
}