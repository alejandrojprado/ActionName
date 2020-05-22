package com.mahisoft.kamino.mssearch.dto

import com.mahisoft.kamino.mssearch.dto.filters.Filter
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import io.swagger.annotations.ApiParam

@ApiModel("Search Request", description = "Search request object used to perform the search")
data class SearchRequestDto(
    @ApiModelProperty(value = "List of filters to apply in the search.", required = true)
    @ApiParam(name = "filters", value = "the filter list")
    val filters: List<Filter>,
    @ApiModelProperty(value = "List of columns to return", required = true)
    @ApiParam(name = "columns", value = "The list containing the columns the user wants to receive in the response")
    val columns: List<String>,
    @ApiModelProperty(value = "Page number", required = false)
    @ApiParam(name = "page", value = "The page number is calculated based on the pageSize parameter.")
    val page: Int? = null,
    @ApiModelProperty(value = "Amount of entries to show per page", required = false)
    @ApiParam(name = "pageSize", value = "The amount of entries you want to return in the response")
    val pageSize: Int? = null,
    @ApiModelProperty(value = "Sort object", required = false)
    @ApiParam(name = "sort", value = "The sort object used to indicate the order of the results.")
    val sort: Sort? = null
)