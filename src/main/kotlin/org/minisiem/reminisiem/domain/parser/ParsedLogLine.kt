package org.minisiem.reminisiem.domain.parser

import java.time.LocalDateTime

data class ParsedLogLine(

    val rawLog: String,

    val logType: String,

    val clientIp: String,

    val occurredAt: LocalDateTime,

    val httpMethod: String? = null,

    val requestPath: String? = null,

    val statusCode: Int? = null,

    val responseSize: Long? = null,

    val userAgent: String? = null

)
