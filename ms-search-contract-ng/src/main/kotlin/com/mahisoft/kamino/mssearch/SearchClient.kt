package com.mahisoft.kamino.mssearch

import com.mahisoft.kamino.commons.rest.http.Constants
import com.mahisoft.kamino.mssearch.dto.ResultPage
import com.mahisoft.kamino.mssearch.dto.SearchRequestDto
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject

class SearchClient(
    private val baseUrl: String,
    private val restTemplate: RestTemplate
) {
    companion object {
        private const val RESOURCE_PATH = Constants.V1 + "/search"
    }

    fun search(searchRequest: SearchRequestDto): ResultPage = restTemplate.postForObject(
            "$baseUrl$RESOURCE_PATH",
            searchRequest,
            ResultPage::class
    )
}