package com.mahisoft.kamino.mssearch.service.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.mahisoft.kamino.mssearch.dto.Message
import com.mahisoft.kamino.mssearch.service.search.domain.MessageDocument
import com.mahisoft.kamino.mssearch.service.search.mapping.MessageMapper
import org.elasticsearch.action.search.SearchResponse
import java.util.Arrays

class Util {
    companion object {
        /**
         * Function to get the result of the query
         */
        fun transformSearchResult(response: SearchResponse, objectMapper: ObjectMapper): List<Message> {
            val searchHit = response.hits.hits
            val messageDocuments = arrayListOf<Message>()
            if (searchHit.isNotEmpty()) {
                Arrays.stream(searchHit)
                        .forEach { hit ->
                            messageDocuments
                                    .add(MessageMapper.toDto(objectMapper
                                            .convertValue(hit.sourceAsMap,
                                                    MessageDocument::class.java)))
                        }
            }
            return messageDocuments
        }
    }
}