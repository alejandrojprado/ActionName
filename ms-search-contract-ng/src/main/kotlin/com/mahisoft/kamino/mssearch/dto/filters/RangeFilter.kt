package com.mahisoft.kamino.mssearch.dto.filters

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.Instant

@ApiModel("Range Filter")
data class RangeFilter(
    @ApiModelProperty(value = "Start date for range filter", required = true)
    val from: Instant,
    @ApiModelProperty(value = "End date for range filter", required = true)
    val to: Instant
)
