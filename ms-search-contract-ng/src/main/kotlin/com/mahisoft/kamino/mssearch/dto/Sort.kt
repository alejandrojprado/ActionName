package com.mahisoft.kamino.mssearch.dto

import com.mahisoft.kamino.mssearch.dto.filters.CustomSortOrder
import io.swagger.annotations.ApiModelProperty

data class Sort(
    @ApiModelProperty(value = "Name of the column to sort the data", required = true)
    val column: String,
    @ApiModelProperty(value = "Sort order to sort the data, can be ASC, DESC", required = true)
    val order: CustomSortOrder
)