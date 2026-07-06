package org.minisiem.reminisiem.domain

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "logs")
class Log(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    var logId: Long? = null,

    @Column(name = "raw_log")
    var rawLog: String,

    @Column(name = "log_type", length = 200)
    var logType: String,

    @Column(name = "client_ip", length = 100)
    var clientIp: String,

    @CreationTimestamp
    @Column(name = "occurred_at")
    var occurredAt: LocalDateTime,

    @Column(name = "collected_at")
    var collectedAt: LocalDateTime? = null,

    @Column(name = "http_method", length = 50)
    var httpMethod: String,

    @Column(name = "request_path", length = 2048)
    var requestPath: String,

    @Column(name = "status_code")
    var statusCode: Int,

    @Column(name = "response_size")
    var responseSize: Long? = null,

    @Column(name = "user_agent", length = 512)
    var userAgent: String? = null

) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Log) return false
            if (logId == null || other.logId == null) return false
            return logId == other.logId
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }