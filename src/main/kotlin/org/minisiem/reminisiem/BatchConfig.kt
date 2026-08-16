package org.minisiem.reminisiem

import org.minisiem.reminisiem.collector.LogFileReader
import org.minisiem.reminisiem.collector.LogItemProcessor
import org.minisiem.reminisiem.collector.OffsetSyncWriteListener
import org.minisiem.reminisiem.domain.FileOffsetRepository
import org.minisiem.reminisiem.domain.Log
import org.minisiem.reminisiem.domain.LogRepository
import org.springframework.batch.core.job.Job
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.job.parameters.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.Step
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.infrastructure.item.data.RepositoryItemWriter
import org.springframework.batch.infrastructure.item.data.builder.RepositoryItemWriterBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager


@Configuration
class BatchConfig {

    private val filePath = "C:/tmp/nginx/access.log" // 테스트용 값

    @Bean
    fun logItemWriter(logRepository: LogRepository): RepositoryItemWriter<Log> {
        return RepositoryItemWriterBuilder<Log>()
            .repository(logRepository)
            .methodName("save")
            .build()
    }

    @Bean
    fun logStep(jobRepository: JobRepository,
                transactionManager : PlatformTransactionManager,
                writer: RepositoryItemWriter<Log>,
                fileOffsetRepository: FileOffsetRepository): Step {
        val reader = LogFileReader(filePath, fileOffsetRepository)
        val offsetSyncWriteListener = OffsetSyncWriteListener(reader, fileOffsetRepository, filePath)

        return StepBuilder("logReaderStep", jobRepository)
            .chunk<String,Log>(10)
            .transactionManager(transactionManager)
            .reader(reader)
            .processor(LogItemProcessor())
            .writer(writer)
            .listener(offsetSyncWriteListener)
            .build()
    }

    @Bean
    fun logFileJob(jobRepository: JobRepository, logStep: Step): Job {
        return JobBuilder("job", jobRepository)
            .incrementer(RunIdIncrementer())
            .start(logStep)
            .build()
    }

}