package com.mahisoft.kamino.mssearch.service.search.repository

import com.mahisoft.kamino.mssearch.service.search.domain.UserColumnEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserColumnRepository : JpaRepository<UserColumnEntity, Long> {
    @Query("SELECT count(u)>0 from UserColumnEntity u WHERE u.columnId = :columnId AND u.userId = :userId")
    fun existsByColumnIdAndUserId(columnId: Long, userId: Long): Boolean

    @Query("SELECT u FROM UserColumnEntity u WHERE u.columnId = :columnId AND u.userId = :userId")
    fun findByColumnIdAndUserId(columnId: Long, userId: Long): UserColumnEntity
}
