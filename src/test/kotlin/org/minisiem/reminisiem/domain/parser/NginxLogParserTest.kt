package org.minisiem.reminisiem.domain.parser

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull


class NginxLogParserTest {
   private val parser = NginxLogParser()

    @Test
    fun `정상적인 Nginx 로그가 들어오면 DTO로 변환된다`() {
        // Given
        val rawLog = """127.0.0.1 - - [15/Aug/2026:18:40:35 +0900] "GET /api/v1/logs HTTP/1.1" 200 1024 "-" "Mozilla/5.0""""

        // When
        val result = parser.parse(rawLog)

        // Then
        assertNotNull(result) // 결과가 null이 아니어야 함
        assertEquals("127.0.0.1", result?.clientIp)
        assertEquals("GET", result?.httpMethod)
        assertEquals(200, result?.statusCode)
        assertEquals(1024L, result?.responseSize)
        assertEquals("Mozilla/5.0", result?.userAgent)
    }

    @Test
    fun `응답크기가 대시이고 유저에이전트가 없으면 null로 안전하게 처리된다`(){

        // Given (맨 뒤에 유저 에이전트가 없고, 사이즈가 - 인 로그)
        val rawLog = """192.168.0.5 - - [15/Aug/2026:18:42:10 +0900] "POST /login HTTP/1.1" 401 -"""

        //when
        var result = parser.parse(rawLog)

        assertNotNull(result)

        // ""(빈문자열)이 아니라 진짜 null이 들어왔는지 확인!
        assertNull(result?.userAgent)
        assertNull(result?.responseSize)
    }

}