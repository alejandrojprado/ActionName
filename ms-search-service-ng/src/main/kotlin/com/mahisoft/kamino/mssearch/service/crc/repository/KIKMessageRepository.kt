package com.mahisoft.kamino.mssearch.service.crc.repository

import com.mahisoft.kamino.mssearch.service.crc.domain.KikMessageEntity
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface KIKMessageRepository : PagingAndSortingRepository<KikMessageEntity, Long>