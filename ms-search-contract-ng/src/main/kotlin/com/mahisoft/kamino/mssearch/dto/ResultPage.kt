package com.mahisoft.kamino.mssearch.dto

import io.swagger.annotations.ApiModelProperty

data class ResultPage(
    @ApiModelProperty("Total documents matching the criteria")
    val total: Long,

    val results: List<Message>
)