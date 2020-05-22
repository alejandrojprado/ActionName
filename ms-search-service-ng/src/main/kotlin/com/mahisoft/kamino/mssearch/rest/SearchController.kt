package com.mahisoft.kamino.mssearch.rest

import com.mahisoft.kamino.commons.rest.http.Constants
import com.mahisoft.kamino.mssearch.dto.ResultPage
import com.mahisoft.kamino.mssearch.dto.SearchRequestDto
import com.mahisoft.kamino.mssearch.service.SearchService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@Controller
@RequestMapping(Constants.V1 + "/search")
@Api(value = Constants.V1 + "/search", description = "Search API")
class SearchController(private val searchService: SearchService) {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val logger = LoggerFactory.getLogger(javaClass.enclosingClass)
    }

    @ApiOperation(value = "Make a new search using the filters.")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Request successful"),
        ApiResponse(code = 400, message = "Validation failed, the dto is not compatible"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = [RequestMethod.POST])
    @ResponseBody
    fun search(
        @RequestBody
        @Validated
        @ApiParam(name = "Search Request", value = "The search request parameter")
        searchRequest: SearchRequestDto
    ): ResultPage = searchService.search(searchRequest)
}