package com.mahisoft.kamino.mssearch.service.search.mapping

import com.mahisoft.kamino.mssearch.dto.Message
import com.mahisoft.kamino.mssearch.service.search.domain.MessageDocument

/**
 * Used to transform documents to dto
 */
object MessageMapper {
    fun toDto(messageDocument: MessageDocument): Message =
        Message(
            uuid = messageDocument.uuid,
            groupId = messageDocument.groupId,
            message = messageDocument.message,
            gigaFlag = messageDocument.gigaFlag,
            locateCity = messageDocument.locateCity,
            locateCountry = messageDocument.locateCountry,
            locateCounty = messageDocument.locateCounty,
            locateLat = messageDocument.locateLat,
            locateLon = messageDocument.locateLon,
            locateRegion = messageDocument.locateRegion,
            rowId = messageDocument.rowId,
            webFlag = messageDocument.webFlag,
            chatType = messageDocument.chatType,
            dateAdded = messageDocument.dateAdded,
            documentFileName = messageDocument.documentFileName,
            firstName = messageDocument.firstName,
            fqdn = messageDocument.fqdn,
            documentMimeType = messageDocument.documentMimeType,
            height = messageDocument.height,
            id = messageDocument.id,
            ipAddress = messageDocument.ipAddress,
            ircChannel = messageDocument.ircChannel,
            ircPort = messageDocument.ircPort,
            ircServer = messageDocument.ircServer,
            isScam = messageDocument.isScam,
            isUc = messageDocument.isUc,
            lastName = messageDocument.lastName,
            licenseId = messageDocument.licenseId,
            date = messageDocument.loggedDate,
            messageDate = messageDocument.messageDate,
            messageId = messageDocument.messageId,
            onlineLast = messageDocument.onlineLast,
            phone = messageDocument.phone,
            senderId = messageDocument.senderId,
            sn = messageDocument.sn,
            statMessage = messageDocument.statMessage,
            statRecipient = messageDocument.statRecipient,
            sysMessage = messageDocument.sysMessage,
            type = messageDocument.type,
            userName = messageDocument.userName,
            width = messageDocument.width,
            network = messageDocument.network,
            groupName = messageDocument.groupName,
            imageDate = messageDocument.imageDate,
            imageId = messageDocument.imageId,
            location = messageDocument.location?.toString(),
            onlineLastIndicator = messageDocument.onlineLastIndicator,
            fdqn = messageDocument.fdqn,
            isBot = messageDocument.isBot,
            channel = messageDocument.channel
        )
}
