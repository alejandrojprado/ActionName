package com.mahisoft.kamino.mssearch

import com.mahisoft.kamino.commons.rest.http.Constants
import com.mahisoft.kamino.mssearch.dto.IndexColumnResponse
import com.mahisoft.kamino.mssearch.dto.UserColumnRequest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.springframework.web.client.getForObject

class ColumnClient(
    private val baseUrl: String,
    private val restTemplate: RestTemplate
) {
    companion object {
        private const val RESOURCE_PATH = Constants.V1 + "/column"
    }

    fun getAllColumns(): IndexColumnResponse = restTemplate.getForObject("$baseUrl$RESOURCE_PATH/all")

    fun getFilterColumns(): IndexColumnResponse = restTemplate.getForObject("$baseUrl$RESOURCE_PATH/filter")

    fun getDisplayColumns(): IndexColumnResponse = restTemplate.getForObject("$baseUrl$RESOURCE_PATH/display")

    fun addUserColumn(userColumnRequest: UserColumnRequest) = restTemplate.postForObject(
        "$baseUrl$RESOURCE_PATH/user/",
        userColumnRequest,
        UserColumnRequest::class.java
    )

    fun deleteUserColumn(userColumnRequest: UserColumnRequest): ResponseEntity<Unit> = restTemplate.exchange(
            "$baseUrl$RESOURCE_PATH/user/",
            HttpMethod.DELETE,
            HttpEntity(userColumnRequest),
            UserColumnRequest::class)

    fun getUserColumns(): IndexColumnResponse = restTemplate.getForObject("$baseUrl$RESOURCE_PATH/user/")
}