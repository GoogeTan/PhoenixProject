package phoenix.utils

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import phoenix.init.PhoenixConfiguration

object LogManager
{
    private val LOGGER = LogManager.getLogger()!!

    fun log(obj : Any, message : String)
    {
        if(PhoenixConfiguration.isDebugMode)
            LOGGER.error("<${obj.javaClass.lastName()}> " + message)
        else
            LOGGER.log(Level.DEBUG, "<${obj.javaClass.lastName()}> " + message)
    }

    fun log(from : String, message : String)
    {
        if(PhoenixConfiguration.isDebugMode)
            LOGGER.error("<$from> $message")
        else
            LOGGER.log(Level.DEBUG, "<$from> $message")
    }

    //fun error(obj : Any, message : String?) = LOGGER.error("<${obj.javaClass.lastName()}> " + (message ?: ""))

    fun error(obj : Any, message : String)
    {
        val e = Exception().stackTrace[2]
        val from = e.className.split(".").last()
        val method = e.methodName
        val line = e.lineNumber
        LOGGER.error("<$from::$method::$line> $message")
    }

    fun error(obj : Any, message : Exception?)
    {
        if(message != null)
            LOGGER.error("Exception in class <${obj.javaClass.lastName()}>: " + message.toString())
    }

    fun error(from : String, message : Exception?)
    {
        if(message != null)
            LOGGER.error("Exception in class <$from>: $message")
    }

    fun error(from : String, message : String) = LOGGER.error("<$from> $message")

    fun errorObjects(obj : Any, vararg objects : Any?)
    {
        var message = ""
        for (i in objects)
            message += " $i"
        error(obj, message)
    }

    fun errorObjects(from : String, vararg objects : Any)
    {
        var message = ""
        for (i in objects)
            message += " $i"
        error(from, message)
    }

    private fun<T> Class<T>.lastName() = this.canonicalName.split(".").last()
}