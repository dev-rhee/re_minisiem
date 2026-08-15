package org.minisiem.reminisiem.collector

import org.springframework.batch.infrastructure.item.ExecutionContext
import org.springframework.batch.infrastructure.item.ItemStreamReader
import java.io.RandomAccessFile


class LogFileReader(private val filePath: String, private val startOffset: Long) : ItemStreamReader<String>{
    private lateinit var raf: RandomAccessFile

    // Step 끝난 뒤 "어디까지 읽었는지" 다른 코드가 꺼내 쓸 수 있게
    var currentOffset: Long = startOffset
        private set

    override fun open(executionContext: ExecutionContext) {
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

}