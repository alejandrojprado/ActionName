package com.mahisoft.kamino.mssearch.service.crc.domain

import org.hibernate.annotations.Type
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "TelegramMessages")
data class TelegramMessageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RowId")
    val rowId: Long? = null,

    @Column(name = "uuid")
    val uuid: String? = null,

    @Column(name = "license_id")
    val licenseId: String? = null,

    @Column(name = "type")
    val type: String? = null,

    @Column(name = "message_id")
    val messageId: Long? = null,

    @Column(name = "chat_type")
    var chatType: String? = null,

    @Column(name = "group_id")
    var groupId: String? = null,

    @Column(name = "group_name")
    var groupName: String? = null,

    @Column(name = "sender_id")
    var senderId: String? = null,

    @Column(name = "user_name")
    var userName: String? = null,

    @Column(name = "first_name")
    var firstName: String? = null,

    @Column(name = "last_name")
    var lastName: String? = null,

    @Column(name = "phone")
    var phone: String? = null,

    @Column(name = "message")
    var message: String? = null,

    @Column(name = "width")
    var width: Int? = null,

    @Column(name = "height")
    var height: Int? = null,

    @Column(name = "image_date")
    var imageDate: Instant? = null,

    @Column(name = "image_id")
    var imageId: String? = null,

    @Column(name = "image_byte_size")
    var imageByteSize: Int? = null,

    @Column(name = "location_lat")
    var locationLat: Double? = null,

    @Column(name = "location_long")
    var locationLong: Double? = null,

    @Column(name = "document_filename")
    var documentFileName: String? = null,

    @Column(name = "document_mimetype")
    var documentMimeType: String? = null,

    @Column(name = "document_size")
    var documentSize: Int? = null,

    @Column(name = "date_added")
    var dateAdded: Instant? = null,

    @Column(name = "message_date")
    var messageDate: Instant? = null,

    @Column(name = "last_online")
    var lastOnline: Instant? = null,

    @Column(name = "last_online_indicator")
    var lastOnlineIndicator: Int? = null,

    @Column(name = "logged_date")
    var loggedDate: String? = null,

    @Column(name = "is_bot", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var isBot: Boolean? = null,

    @Column(name = "is_scam", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var isScam: Boolean? = null,

    @Column(name = "online_last")
    var onlineLast: String? = null,

    @Column(name = "online_past_indicator")
    var onlinePastIndicator: String? = null
)