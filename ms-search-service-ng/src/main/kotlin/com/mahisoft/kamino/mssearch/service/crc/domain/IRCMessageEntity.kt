package com.mahisoft.kamino.mssearch.service.crc.domain

import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "IRCChatHistory")
data class IRCMessageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "date")
    val date: Instant? = null,

    @Column(name = "ircserver")
    val ircServer: String? = null,

    @Column(name = "ircport")
    val ircPort: Int? = null,

    @Column(name = "irchannel")
    val ircChannel: String? = null,

    @Column(name = "ircnick")
    val ircNick: String? = null,

    @Column(name = "ircuser")
    val ircUser: String? = null,

    @Column(name = "fqdn")
    val fqdn: String? = null,

    @Column(name = "ipaddress")
    val ipAddress: String? = null,

    @Column(name = "message")
    val message: String? = null,

    @Column(name = "LocateCountry")
    val locateCountry: String? = null,

    @Column(name = "LocateRegion")
    val locateRegion: String? = null,

    @Column(name = "LocateCity")
    val locateCity: String? = null,

    @Column(name = "LocateLat")
    val locateLat: Float? = null,

    @Column(name = "LocateLon")
    val locateLon: Float? = null,

    @Column(name = "LocateCounty")
    val locateCounty: String? = null,

    @Column(name = "GigaFlag")
    val gigaFlag: String? = null,

    @Column(name = "WebFlag")
    val webFlag: String? = null,

    @Column(name = "uuid")
    val uuid: String? = null
)