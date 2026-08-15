package org.minisiem.reminisiem.domain

import org.springframework.data.jpa.repository.JpaRepository

interface LogRepository  : JpaRepository<Log, Long> {

}