package com.mahisoft.kamino.mssearch.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ApiModel("User Column")
data class UserSearch(

    @ApiModelProperty(value = "User Search Id", required = true, readOnly = true)
    val id: Long? = null,

    @ApiModelProperty(value = "User Search Name", required = true)
    @field:NotNull(message = "Search Name is required")
    @field:Size(min = 1, max = 255, message = "Search Name is required and can't be empty. Search Name max size is 255 chars")
    val searchName: String,

    @ApiModelProperty(value = "Search DTO", required = true)
    @field:NotNull(message = "dto is required")
    @field:Size(min = 1, max = 255, message = "dto is required and can't be empty. dto max size is 15000 chars")
    val dto: String,

    @ApiModelProperty(value = "User Id", required = true, readOnly = true)
    val userId: Long

)