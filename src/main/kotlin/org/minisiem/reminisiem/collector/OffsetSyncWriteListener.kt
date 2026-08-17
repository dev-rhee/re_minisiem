package org.minisiem.reminisiem.collector

import org.minisiem.reminisiem.domain.FileOffSet
import org.minisiem.reminisiem.domain.FileOffsetRepository
import org.minisiem.reminisiem.domain.Log
import org.springframework.batch.core.listener.ItemWriteListener
import org.springframework.batch.infrastructure.item.Chunk
import java.time.LocalDateTime

// afterWrite는 writer의 트랜잭션 커밋 전에 호출되므로,
// 여기서 offset을 저장하면 로그 insert와 offset 갱신이 같은 트랜잭션으로 묶여
// 하나가 롤백되면 나머지도 함께 롤백된다. (chunk 단위 원자성 보장)
class OffsetSyncWriteListener(
    private val reader: LogFileReader,
    private val fileOffsetRepository: FileOffsetRepository
) : ItemWriteListener<Log> {

    override fun afterWrite(items: Chunk<out Log>) {
        val existing = fileOffsetRepository.findByFilePath(reader.filePath)
        if (existing != null) {
            existing.byteOffset = reader.currentOffset
            fileOffsetRepository.save(existing)
        } else {
            fileOffsetRepository.save(
                FileOffSet(filePath = reader.filePath, byteOffset = reader.currentOffset, lastReadAt = LocalDateTime.now())
            )
        }
    }
}
