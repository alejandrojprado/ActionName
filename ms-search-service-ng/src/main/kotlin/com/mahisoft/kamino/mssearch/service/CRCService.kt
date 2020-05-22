package com.mahisoft.kamino.mssearch.service

import com.mahisoft.kamino.mssearch.service.crc.domain.IRCMessageEntity
import com.mahisoft.kamino.mssearch.service.crc.domain.KikMessageEntity
import com.mahisoft.kamino.mssearch.service.crc.domain.TelegramMessageEntity
import com.mahisoft.kamino.mssearch.service.crc.repository.IRCMessageRepository
import com.mahisoft.kamino.mssearch.service.crc.repository.KIKMessageRepository
import com.mahisoft.kamino.mssearch.service.crc.repository.TelegramMessageRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

/**
 * Service to perform queries over elastic search index
 */
@Service
class CRCService(
    val telegramMessageRepository: TelegramMessageRepository,
    val kikMessageRepository: KIKMessageRepository,
    val ircMessageRepository: IRCMessageRepository
) {

    companion object {
        const val SINGLE_VALUE = 1

        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val logger = LoggerFactory.getLogger(javaClass.enclosingClass)
    }

    /**
     * Called to get all the messages from the telegram table
     *
     * @param page page number
     * @param size the amount of results to return
     *
     * @return a Page object with the telegram messages
     */
    fun getTelegramMessages(page: Int, size: Int): Page<TelegramMessageEntity> {
        return telegramMessageRepository.findAll(PageRequest.of(page, size))
    }

    /**
     * Called to get all the messages from the KIK table
     *
     * @param page page number
     * @param size the amount of results to return
     *
     * @return a Page object with the KIK messages
     */
    fun getKIKMessages(page: Int, size: Int): Page<KikMessageEntity> {
        return kikMessageRepository.findAll(PageRequest.of(page, size))
    }

    /**
     * Called to get all the messages from the IRC table
     *
     * @param page page number
     * @param size the amount of results to return
     *
     * @return a Page object with the IRC messages
     */
    fun getIRCMessages(page: Int, size: Int): Page<IRCMessageEntity> {
        return ircMessageRepository.findAll(PageRequest.of(page, size))
    }
}