package org.minisiem.reminisiem.collector

import org.minisiem.reminisiem.domain.Log
import org.minisiem.reminisiem.domain.parser.NginxLogParser
import org.springframework.batch.infrastructure.item.ItemProcessor


class LogItemProcessor : ItemProcessor<String, Log> {
    val parser = NginxLogParser()
    override fun process(item: String): Log? {
        val parsedLogLine  = parser.parse(item)?: return null
        return Log  (
            rawLog= parsedLogLine.rawLog,
            logType = parsedLogLine.logType,
            clientIp = parsedLogLine.clientIp,
            occurredAt = parsedLogLine.occurredAt,
            httpMethod = parsedLogLine.httpMethod,
            requestPath = parsedLogLine.requestPath,
            statusCode = parsedLogLine.statusCode,
            responseSize = parsedLogLine.responseSize,
            userAgent = parsedLogLine.userAgent

                )
    }

}