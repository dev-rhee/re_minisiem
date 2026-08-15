package org.minisiem.reminisiem.collector

import org.minisiem.reminisiem.domain.FileOffSet
import org.minisiem.reminisiem.domain.FileOffsetRepository
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.listener.StepExecutionListener
import org.springframework.batch.core.step.StepExecution
import org.springframework.batch.infrastructure.item.ExecutionContext
import org.springframework.batch.infrastructure.item.ItemStreamReader
import java.io.RandomAccessFile
import java.time.LocalDateTime


class LogFileReader(
    private val filePath: String,
    private val fileOffsetRepository: FileOffsetRepository
) : ItemStreamReader<String>, StepExecutionListener {
    private lateinit var raf: RandomAccessFile

    // Step 끝난 뒤 "어디까지 읽었는지" 다른 코드가 꺼내 쓸 수 있게
    var currentOffset: Long = 0L
        private set

    override fun open(executionContext: ExecutionContext) {
        val startOffset = fileOffsetRepository.findByFilePath(filePath)?.byteOffset ?: 0L
        currentOffset = startOffset
        raf = RandomAccessFile(filePath, "r")
        raf.seek(startOffset)          // 딱 한 번만, 여기서
    }

    override fun read(): String? {
        val line = raf.readLine()      // 호출될 때마다 자동으로 다음 줄로 이어짐
        if (line != null) {
            currentOffset = raf.filePointer   // 어디까지 읽었는지 계속 갱신
        }
        return line
    }

    override fun update(executionContext: ExecutionContext) {
        super.update(executionContext)
    }

    override fun close() {
        raf.close()
    }

    override fun afterStep(stepExecution: StepExecution): ExitStatus? {
        if (stepExecution.status != BatchStatus.COMPLETED) {
            return null
        }
        val existing = fileOffsetRepository.findByFilePath(filePath)
        if (existing != null) {
            existing.byteOffset = currentOffset
            fileOffsetRepository.save(existing)
        } else {
            fileOffsetRepository.save(
                FileOffSet(filePath = filePath, byteOffset = currentOffset, lastReadAt = LocalDateTime.now())
            )
        }
        return null
    }

}