package com.wjb.klog

import android.util.Log
import java.lang.reflect.Method
import java.util.Stack

object KLog {
    private val logMessageList = mutableListOf<LogMessage>()
    private val methodMap = hashMapOf<String, Method>()
    var enable = true
    fun log(logType: LogType, tag: String = "TAG", contentScope: KLog.() -> Unit) {
        if (!enable) return
        contentScope(this)
        val methodName = when (logType) {
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
        val divider="-".repeat(10)
        method.invoke(null, tag, "$divider$tag$divider")
        logMessageList.forEach { (description, content) ->
            method.invoke(
                null,
                tag,
                "$description -> ${if (content is Iterable<*>) content.joinToString() else content}"
            )
        }
        method.invoke(null, tag, "$divider$tag$divider")
        logMessageList.clear()
    }

    infix fun String.with(content: Any?) {
        logMessageList.add(LogMessage(this, content))
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