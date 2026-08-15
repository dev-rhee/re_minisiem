package org.minisiem.reminisiem.domain

import org.springframework.data.jpa.repository.JpaRepository

interface FileOffsetRepository : JpaRepository<FileOffSet, Long> {
    fun findByFilePath(filePath: String): FileOffSet?

}