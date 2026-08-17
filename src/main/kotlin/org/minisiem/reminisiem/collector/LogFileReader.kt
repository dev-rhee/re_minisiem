package org.minisiem.reminisiem.collector

import org.minisiem.reminisiem.domain.FileOffsetRepository
import org.springframework.batch.infrastructure.item.ExecutionContext
import org.springframework.batch.infrastructure.item.ItemStreamReader
import java.io.RandomAccessFile


class LogFileReader(
    val filePath: String,
    private val fileOffsetRepository: FileOffsetRepository
) : ItemStreamReader<String> {
    private lateinit var raf: RandomAccessFile

    // Step 끝난 뒤 어디까지 읽었는지 다른 코드가 꺼내 쓸 수 있게
    var currentOffset: Long = 0L
        private set

    override fun open(executionContext: ExecutionContext) {
        raf = RandomAccessFile(filePath, "r")

        val savedOffset = fileOffsetRepository.findByFilePath(filePath)?.byteOffset ?: 0L
        // 로그 로테이션 등으로 파일이 교체되면 저장된 offset이 새 파일 크기보다 커질 수 있다.
        // 이 경우 이어 읽을 수 없으므로 처음부터 다시 읽는다.
        val startOffset = if (savedOffset > raf.length()) 0L else savedOffset

        currentOffset = startOffset
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

}