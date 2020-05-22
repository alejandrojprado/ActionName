package com.mahisoft.kamino.mssearch.service.crc.repository

import com.mahisoft.kamino.mssearch.service.crc.domain.IRCMessageEntity
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface IRCMessageRepository : PagingAndSortingRepository<IRCMessageEntity, Long>