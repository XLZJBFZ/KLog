package com.wjb.klog

import android.util.Log
import java.lang.reflect.Method

object KLog {
    private val methodMap = hashMapOf<String, Method>()
    var enable = true
    fun log(
        contentScope: KLogBuilder.() -> Unit
    ) {
        if (!enable) return
        val kLogInstance = KLogBuilder()
        contentScope(kLogInstance)
        if (!kLogInstance.enable) return
        val methodName = when (kLogInstance.logType) {
            LogType.ASSERT -> "wtf"
            LogType.DEBUG -> "d"
            LogType.ERROR -> "e"
            LogType.INFO -> "i"
            LogType.VERBOSE -> "v"
            LogType.WARN -> "w"
        }
        if (methodMap[methodName] == null) {
            methodMap[methodName] = Log::class.java.getMethod(
                methodName,
                String::class.java,
                String::class.java
            )
        }
        val method: Method = methodMap[methodName]!!
        kLogInstance.run{
            divider?.let {
                method.invoke(null, tag, it)
            }
            logMessageList.forEach { (description, content) ->
                method.invoke(
                    null,
                    tag,
                    "$description $connectionSymbol ${converter(content)}"
                )
            }
            divider?.let {
                method.invoke(null, tag, it)
            }
        }
    }

    class KLogBuilder {
        var enable = true
        var logType: LogType = LogType.DEBUG
        var tag = "TAG"
        var divider: String? = "-".repeat(10) + tag + "-".repeat(10)
        var connectionSymbol = "->"
        var converter: (Any?) -> String = {
            if (it is Iterable<*>) it.joinToString() else it.toString()
        }
        val logMessageList = mutableListOf<LogMessage>()
        infix fun String.with(content: Any?) {
            logMessageList.add(LogMessage(this, content))
        }
    }

    sealed class LogType {
        object VERBOSE : LogType()
        object DEBUG : LogType()
        object INFO : LogType()
        object WARN : LogType()
        object ERROR : LogType()
        object ASSERT : LogType()
    }

    data class LogMessage(val description: String, val content: Any?)
}
