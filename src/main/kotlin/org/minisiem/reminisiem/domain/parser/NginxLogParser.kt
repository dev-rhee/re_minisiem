package org.minisiem.reminisiem.domain.parser

import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class NginxLogParser : LogParser {
    private val log = LoggerFactory.getLogger(javaClass)
    val pattern  = """^(\S+)\s+\S+\s+\S+\s+\[([^\]]+)\]\s+"(\w+)\s+(\S+)\s+\S+"\s+(\d{3})\s+(\d+|-)(?:\s+"[^"]*"\s+"([^"]*)")?${'$'}""".toRegex()
    val logType = "NGINX"
    // Nginx 타임존 포맷 전용 포매터 정의
    private val nginxTimeFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH)

    override fun parse(rawLog: String): ParsedLogLine? {
        val parsedLog: ParsedLogLine?
        val matchResult = pattern.find(rawLog)

        if (matchResult != null) {

            val (ip, time, method, path, status, size, ua) = matchResult.destructured;

             parsedLog = ParsedLogLine(
                rawLog,
                logType = logType,
                clientIp = ip,
                occurredAt = LocalDateTime.parse(time, nginxTimeFormatter),
                httpMethod = method,
                requestPath = path,
                statusCode = status.toInt(), // String을 Int로 변환해서 쏙!
                responseSize = size.toLongOrNull() ,
                userAgent  = ua.takeIf { it.isNotEmpty() }
            )

        } else {
            log.warn("로그 포맷이 일치하지 않아 파싱할 수 없습니다: {}", rawLog)
            return null
        }

        return parsedLog
    }
}