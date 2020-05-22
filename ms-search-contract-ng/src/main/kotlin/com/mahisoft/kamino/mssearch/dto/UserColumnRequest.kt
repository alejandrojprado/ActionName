package com.mahisoft.kamino.mssearch.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("User Column Request", description = "Used for user/column related operations")
data class UserColumnRequest(
    @ApiModelProperty("Column code")
    val columnCode: String? = null
)