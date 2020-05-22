package com.mahisoft.kamino.mssearch.service.search.repository

import com.mahisoft.kamino.mssearch.service.search.domain.IndexColumnEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface IndexColumnRepository : JpaRepository<IndexColumnEntity, Long> {

    @Query("SELECT c FROM IndexColumnEntity c WHERE c.active = 1")
    fun findActiveColumns(): List<IndexColumnEntity>

    @Query("SELECT c FROM IndexColumnEntity c WHERE c.active = 1 AND c.isFilterColumn = 1")
    fun findFilterColumns(): List<IndexColumnEntity>

    @Query("SELECT c FROM IndexColumnEntity c WHERE c.active = 1 AND c.isDisplayColumn = 1")
    fun findDisplayColumns(): List<IndexColumnEntity>

    @Query("SELECT c FROM IndexColumnEntity c, UserColumnEntity u WHERE c.active = 1 AND c.id = u.columnId AND u.userId=:userId")
    fun findUserColumns(userId: Long): List<IndexColumnEntity>

    fun existsByColumnCode(columnCode: String): Boolean

    fun findByColumnCode(columnCode: String): IndexColumnEntity
}
