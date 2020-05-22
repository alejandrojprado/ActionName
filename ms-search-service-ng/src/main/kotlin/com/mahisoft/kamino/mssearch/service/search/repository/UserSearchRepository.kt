package com.mahisoft.kamino.mssearch.service.search.repository

import com.mahisoft.kamino.mssearch.service.search.domain.IndexColumnEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserSearchRepository : JpaRepository<IndexColumnEntity, Long>
