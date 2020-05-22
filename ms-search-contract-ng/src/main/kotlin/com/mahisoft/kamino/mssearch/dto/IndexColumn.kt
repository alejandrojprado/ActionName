package com.mahisoft.kamino.mssearch.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ApiModel("Index Column")
data class IndexColumn(

    @ApiModelProperty(value = "Index Column Code", required = true)
    @field:NotNull(message = "Column Code is required")
    @field:Size(min = 1, max = 255, message = "Column Code is required and can't be empty. Column Code max size is 255 chars")
    val columnCode: String,

    @ApiModelProperty(value = "Index Column Name", required = true)
    @field:NotNull(message = "Column Name is required")
    @field:Size(min = 1, max = 255, message = "Column Name is required and can't be empty. Column Name max size is 255 chars")
    val columnName: String
)