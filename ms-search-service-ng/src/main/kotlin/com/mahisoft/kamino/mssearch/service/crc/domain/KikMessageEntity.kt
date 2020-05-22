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
@Table(name = "KikMessages")
data class KikMessageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RowId")
    val rowId: Long? = null,

    @Column(name = "uuid")
    val uuid: String? = null,

    @Column(name = "license_id")
    val licenseId: String? = null,

    @Column(name = "sender_id")
    var senderId: String? = null,

    @Column(name = "group_id")
    var groupId: String? = null,

    @Column(name = "message")
    var message: String? = null,

    @Column(name = "sysmessage")
    var sysMessage: String? = null,

    @Column(name = "statmessage")
    var statMessage: String? = null,

    @Column(name = "statrecipient")
    var statrecipient: String? = null,

    @Column(name = "is_uc", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var isUc: Boolean? = null,

    @Column(name = "date_added")
    var dateAdded: Instant? = null,

    @Column(name = "message_date")
    var messageDate: Instant? = null
)