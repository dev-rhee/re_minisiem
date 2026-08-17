package org.minisiem.reminisiem

import org.slf4j.LoggerFactory
import org.springframework.batch.core.job.Job
import org.springframework.batch.core.launch.JobOperator
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class LogCollectionScheduler(
    private val jobOperator: JobOperator,
    private val job: Job
)  {
    private val log = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedRate = 60000)
    fun runJob() {
        try {
            jobOperator.startNextInstance(job)
        } catch (e: Exception) {
            log.error("로그 수집 Job 실행 실패", e)
        }
    }
}