package org.minisiem.reminisiem.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "file_offsets")
class FileOffSet (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "file_path", length = 500)
    var filePath: String,

    @Column(name = "byte_offset")
    var byteOffset: Long,

    @UpdateTimestamp
    @Column(name="last_read_at")
    var lastReadAt : LocalDateTime,

    ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FileOffSet) return false
        if (id == null || other.id == null) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}