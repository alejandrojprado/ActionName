package com.mahisoft.kamino.mssearch.dto.filters

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel("Filter object")
data class Filter(
    @ApiModelProperty(value = "Column name in the elastic search index", required = true)
    val key: String,
    @ApiModelProperty(value = "The type of the filter, TEXT, RANGE", required = true)
    val filterType: FilterType,
    @ApiModelProperty(value = """
        The type of the match you need, for example EXACT is to look for exact terms in a field,
        IMPORTANT! this parameter is only used when you do a TEXT search, in the case of a RANGE
        search you don't need to sent this value.
        IMPORTANT! the size of 'matchType' has to match the size of 'values' in the case of a TEXT search
    """, required = false)
    val matchType: List<MatchType>? = null,
    @ApiModelProperty(value = """
        List of values to search.
        IMPORTANT! this parameter is only used when you do a TEXT search, in the case of a RANGE
        search you don't need to sent this value.
        IMPORTANT! the size of 'matchType' has to match the size of 'values' in the case of a TEXT search
    """, required = false)
    val values: List<String>? = null,
    @ApiModelProperty(value = """
        List of range values, this value is only used in the case of a RANGE search.
    """, required = false)
    val rangeValues: List<RangeFilter>? = null
)
