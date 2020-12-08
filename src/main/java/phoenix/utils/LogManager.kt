package phoenix.utils

import org.apache.logging.log4j.Level
import phoenix.Phoenix
import phoenix.init.PhoenixConfiguration

object LogManager
{
    fun log(obj : Any, message : String) : Unit
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

    fun error(obj : Any, message : String) : Unit
    {
        Phoenix.LOGGER.error("${obj.javaClass} " + message)
    }
}