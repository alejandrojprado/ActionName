package com.mahisoft.kamino.mssearch.dto

import io.swagger.annotations.ApiModel

@ApiModel("Index Column Response")
data class IndexColumnResponse(
    val columns: List<IndexColumn>
)