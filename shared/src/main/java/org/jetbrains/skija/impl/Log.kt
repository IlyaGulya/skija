package org.jetbrains.skija.impl

import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.function.Supplier

object Log {
    internal var _level = 0
    fun trace(s: String) {
        _log(1, "[TRACE]", s)
    }

    fun debug(s: String) {
        _log(2, "[DEBUG]", s)
    }

    fun info(s: String) {
        _log(3, "[INFO ]", s)
    }

    fun warn(s: String) {
        _log(4, "[WARN ]", s)
    }

    fun error(s: String) {
        _log(5, "[ERROR]", s)
    }

    fun trace(s: Supplier<String>) {
        _log(1, "[TRACE]", s)
    }

    fun debug(s: Supplier<String>) {
        _log(2, "[DEBUG]", s)
    }

    fun info(s: Supplier<String>) {
        _log(3, "[INFO ]", s)
    }

    fun warn(s: Supplier<String>) {
        _log(4, "[WARN ]", s)
    }

    fun error(s: Supplier<String>) {
        _log(5, "[ERROR]", s)
    }

    fun _time(): String {
        return ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)
    }

    fun _log(level: Int, prefix: String, s: String) {
        if (level >= _level) println(_time() + " " + prefix + " " + s)
    }

    fun _log(level: Int, prefix: String, s: Supplier<String>) {
        if (level >= _level) println(_time() + " " + prefix + " " + s.get())
    }

    init {
        val property = System.getProperty("skija.logLevel")
        _level =
            when (property) {
                "ALL" -> 0
                "TRACE" -> 1
                "DEBUG" -> 2
                "INFO" -> 3
                null, "WARN" -> 4
                "ERROR" -> 5
                "NONE" -> 6
                else -> throw IllegalArgumentException("Unknown log level: " + property)
            }
    }
}