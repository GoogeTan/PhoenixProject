package phoenix.utils

import org.apache.logging.log4j.Level
import phoenix.Phoenix
import phoenix.init.PhoenixConfiguration

object LogManager
{
    @JvmStatic
    fun log(obj : Any, message : String)
    {
        if(PhoenixConfiguration.COMMON_CONFIG.debug.get())
        {
            Phoenix.LOGGER.error("${obj.javaClass} " + message)
        }
        else
        {
            Phoenix.LOGGER.log(Level.DEBUG, "${obj.javaClass} " + message)
        }
    }

    @JvmStatic
    fun error(obj : Any, message : String?)
    {
        Phoenix.LOGGER.error("${obj.javaClass} " + (message ?: ""))
    }

    @JvmStatic
    fun error(obj : Any, message : Exception?)
    {
        if(message != null)
            Phoenix.LOGGER.error("Exception in class ${obj.javaClass.name}: " + message.toString())
    }

    @JvmStatic
    fun errorObjects(obj : Any, vararg objects : Any)
    {
        var message = ""
        for (i in objects)
            message += " $i"
        error(obj, message)
    }
}