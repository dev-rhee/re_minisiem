package org.minisiem.reminisiem.domain.parser

interface LogParser {
    fun parse(rawLog: String): ParsedLogLine?
}