package com.mahisoft.kamino.mssearch.service.search.domain

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.elasticsearch.common.geo.GeoPoint
import org.springframework.data.annotation.Id
import java.time.Instant

/**
 * Main document for index messages
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class MessageDocument(
    @Id
    @JsonProperty("id")
    val id: Long? = null,
    @JsonProperty("uuid")
    val uuid: String? = null,
    @JsonProperty("group_id")
    val groupId: String? = null,
    @JsonProperty("group_name")
    val groupName: String? = null,
    @JsonProperty("message")
    val message: String? = null,
    @JsonProperty("giga_flag")
    val gigaFlag: String? = null,
    @JsonProperty("locate_city")
    val locateCity: String? = null,
    @JsonProperty("locate_country")
    val locateCountry: String? = null,
    @JsonProperty("locate_county")
    val locateCounty: String? = null,
    @JsonProperty("LocateLat")
    val locateLat: Float? = null,
    @JsonProperty("LocateLon")
    val locateLon: Float? = null,
    @JsonProperty("locate_region")
    val locateRegion: String? = null,
    @JsonProperty("RowId")
    val rowId: Long? = null,
    @JsonProperty("web_flag")
    val webFlag: String? = null,
    @JsonProperty("chat_type")
    val chatType: String? = null,
    @JsonProperty("date_added")
    val dateAdded: String? = null,
    @JsonProperty("document_filename")
    val documentFileName: String? = null,
    @JsonProperty("first_name")
    val firstName: String? = null,
    @JsonProperty("fqdn")
    val fqdn: String? = null,
    @JsonProperty("document_mimetype")
    val documentMimeType: String? = null,
    @JsonProperty("height")
    val height: Long? = null,
    @JsonProperty("ipaddress")
    val ipAddress: String? = null,
    @JsonProperty("irchannel")
    val ircChannel: String? = null,
    @JsonProperty("ircport")
    val ircPort: Long? = null,
    @JsonProperty("ircserver")
    val ircServer: String? = null,
    @JsonProperty("is_bot")
    val isBot: Boolean? = null,
    @JsonProperty("is_scam")
    val isScam: Boolean? = null,
    @JsonProperty("is_uc")
    val isUc: Boolean? = null,
    @JsonProperty("last_name")
    val lastName: String? = null,
    @JsonProperty("license_id")
    val licenseId: String? = null,
    @JsonProperty("logged_date")
    val loggedDate: Instant? = null,
    @JsonProperty("message_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val messageDate: String? = null,
    @JsonProperty("image_date")
    val imageDate: Instant? = null,
    @JsonProperty("image_id")
    val imageId: String? = null,
    @JsonProperty("message_id")
    val messageId: Long? = null,
    @JsonProperty("online_last")
    val onlineLast: String? = null,
    @JsonProperty("phone")
    val phone: String? = null,
    @JsonProperty("sender_id")
    val senderId: String? = null,
    @JsonProperty("sn")
    val sn: String? = null,
    @JsonProperty("statmessage")
    val statMessage: String? = null,
    @JsonProperty("statrecipient")
    val statRecipient: String? = null,
    @JsonProperty("sysmessage")
    val sysMessage: String? = null,
    @JsonProperty("type")
    val type: String? = null,
    @JsonProperty("user_name")
    val userName: String? = null,
    @JsonProperty("width")
    val width: Long? = null,
    @JsonProperty("network")
    val network: String? = null,
    @JsonProperty("location")
    val location: GeoPoint? = null,
    @JsonProperty("online_last_indicator")
    val onlineLastIndicator: String? = null,
    @JsonProperty("fdqn")
    val fdqn: String? = null,
    @JsonProperty("channel")
    val channel: String? = null
)
