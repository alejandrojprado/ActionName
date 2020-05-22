package com.mahisoft.kamino.mssearch.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.mahisoft.kamino.mssearch.config.ElasticSearchColumnsConfiguration
import com.mahisoft.kamino.mssearch.dto.ResultPage
import com.mahisoft.kamino.mssearch.dto.SearchRequestDto
import com.mahisoft.kamino.mssearch.dto.filters.CustomSortOrder
import com.mahisoft.kamino.mssearch.dto.filters.Filter
import com.mahisoft.kamino.mssearch.dto.filters.FilterType
import com.mahisoft.kamino.mssearch.dto.filters.MatchType
import com.mahisoft.kamino.mssearch.dto.filters.RangeFilter
import com.mahisoft.kamino.mssearch.exception.CustomException
import com.mahisoft.kamino.mssearch.service.util.Util.Companion.transformSearchResult
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.index.query.BoolQueryBuilder
import org.elasticsearch.index.query.Operator
import org.elasticsearch.index.query.QueryBuilder
import org.elasticsearch.index.query.QueryBuilders.boolQuery
import org.elasticsearch.index.query.QueryBuilders.matchQuery
import org.elasticsearch.index.query.QueryBuilders.rangeQuery
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.sort.SortOrder
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * Service to perform queries over elastic search index
 */
@Service
class SearchService(
    val client: RestHighLevelClient,
    val objectMapper: ObjectMapper,
    val elasticSearchColumnsConfiguration: ElasticSearchColumnsConfiguration
) {

    companion object {
        const val SINGLE_VALUE = 1

        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val logger = LoggerFactory.getLogger(javaClass.enclosingClass)
    }

    /**
     * Called to make a search using the filters received from the controller
     *
     * @param searchRequest an instance with the filters of the search
     *
     * @return a ResultPage instance with the results
     */
    fun search(searchRequest: SearchRequestDto): ResultPage {
        return applySearch(createQuery(searchRequest), searchRequest)
    }

    /**
     * Called to iterate over the received filter and create a query with the required filters
     *
     * @param searchRequestDto an instance with the filters of the search
     *
     * @return a BoolQueryBuilder instance representing the query
     */
    private fun createQuery(searchRequestDto: SearchRequestDto): BoolQueryBuilder {
        val baseQuery = boolQuery()
        for (filter in searchRequestDto.filters) {
            getQueryBuilderByType(filter)?.let {
                baseQuery.must(it)
            }
        }
        return baseQuery
    }

    /**
     * Called to get a query builder based on the type of the filter
     *
     * @param filter a filter object
     *
     * @return a QueryBuilder instance
     */
    fun getQueryBuilderByType(filter: Filter): QueryBuilder? {
        return when (filter.filterType) {
            FilterType.TEXT -> {
                addTextQuery(filter)
            }
            FilterType.RANGE -> {
                addRangeQuery(filter)
            }
        }
    }

    /**
     * Called to get a queryBuilder based on the MatchType
     *
     * @param key the name of the column in the index
     * @param value the value to search for
     * @param matchType the type of the query to return
     *
     * @return a QueryBuilder instance
     */
    fun getQueryByType(key: String, value: String, matchType: MatchType): QueryBuilder {
        return when (matchType) {
            MatchType.EXACT -> {
                matchQuery(key, value.toLowerCase()).operator(Operator.AND)
            }
            MatchType.FUZZY -> {
                matchQuery(key, value.toLowerCase()).fuzziness(2).prefixLength(1).operator(Operator.AND)
            }
            MatchType.CONTAINED -> {
                matchQuery(checkContainedKey(key), value.toLowerCase()).operator(Operator.AND)
            }
        }
    }

    private fun checkContainedKey(key: String) =
            if (elasticSearchColumnsConfiguration.containedKeys.contains(key)) "$key.contained" else key

    /**
     * Called to create a TEXT query from a list of values, the properties 'values' and 'matchType' should have the
     * same size if not the the functions is going to throw a CustomException, if multiple values are sent for the same
     * key then the query is created as a should <OR>.
     *
     * @param filter the Filter instance with all the information of the request
     *
     * @return a QueryBuilder builder object, return null in case the values are empty or null
     *
     */
    fun addTextQuery(filter: Filter): QueryBuilder? {
        if (filter.values?.size != filter.matchType?.size)
            throw CustomException("The property values and matchType should have the same length.")

        filter.values?.let {
            if (it.isNotEmpty()) {
                return if (filter.values!!.size == SINGLE_VALUE) {
                    getQueryByType(filter.key, filter.values!!.first(), filter.matchType!!.first())
                } else {
                    val tempQueryBuilder = boolQuery()
                    filter.values!!.forEachIndexed { i, value ->
                        tempQueryBuilder.should(getQueryByType(filter.key, value, filter.matchType!![i]))
                    }
                    tempQueryBuilder
                }
            }
        }
        return null
    }

    /**
     * Called to create a RANGE query from a list of values, if the list has only a single value then returns a
     * query of the type rangeQuery, if it has multiple values then it returns a boolQuery with a chain of 'should'
     * with the type of rangeQuery.
     *
     * @param filter the Filter instance with all the information of the request
     *
     * @return a QueryBuilder builder object, return null in case the values are empty or null
     *
     */
    fun addRangeQuery(filter: Filter): QueryBuilder? {
        filter.rangeValues?.let {
            if (it.isNotEmpty()) {
                return if (filter.rangeValues!!.size == SINGLE_VALUE) {
                    getRangeFilter(filter.key, filter.rangeValues!!.first())
                } else {
                    val tempQueryBuilder = boolQuery()
                    for (rangeValue in filter.rangeValues!!) {
                        tempQueryBuilder.should(getRangeFilter(filter.key, rangeValue))
                    }
                    tempQueryBuilder
                }
            }
        }
        return null
    }

    /**
     * Called to build a rangeQuery using the filter information
     *
     * @param key is the name of the column in the index
     * @param rangeFilter is the instance of the rangeQuery from, to
     *
     * @return a rangeQuery instance
     */
    fun getRangeFilter(key: String, rangeFilter: RangeFilter): QueryBuilder = rangeQuery(key)
            .gte(rangeFilter.from)
            .lte(rangeFilter.to)

    /**
     * Called to execute the query and return the results
     *
     * @param queryBuilder the query to execute
     * @param searchRequestDto the search request received from the user
     */
    private fun applySearch(queryBuilder: QueryBuilder, searchRequestDto: SearchRequestDto): ResultPage {
        logger.info("Query: $queryBuilder")
        val searchRequest = SearchRequest()
        val searchSourceBuilder = SearchSourceBuilder()
        val includeColumns = searchRequestDto.columns

        searchSourceBuilder.query(queryBuilder)
        setPageParameters(searchRequestDto, searchSourceBuilder)
        searchRequestDto.sort?.let {
            searchSourceBuilder.sort(it.column, getSortOrder(it.order))
        }
        if (includeColumns.isNotEmpty())
            searchSourceBuilder.fetchSource(includeColumns.toTypedArray(), arrayOf())

        searchRequest.source(searchSourceBuilder)
        val searchResponse = client.search(searchRequest, RequestOptions.DEFAULT)
        return ResultPage(searchResponse.hits.totalHits, transformSearchResult(searchResponse, objectMapper))
    }

    /**
     * Called to map the custom sort order to elasticsearch sort order
     *
     * @param sortOrder the value of the sort order
     *
     * @return a SortOrder value of elasticsearch
     */
    fun getSortOrder(sortOrder: CustomSortOrder): SortOrder {
        return when (sortOrder) {
            CustomSortOrder.ASC -> {
                SortOrder.ASC
            }
            CustomSortOrder.DESC -> {
                SortOrder.DESC
            }
            else -> {
                SortOrder.ASC
            }
        }
    }

    /**
     * Called to set the pagination parameters, the pagination needs to parameters the page number 1,2,3 and the amount
     * of results per page
     *
     * @param searchRequestDto the search request received from the user
     * @param searchSourceBuilder
     *
     */
    private fun setPageParameters(searchRequestDto: SearchRequestDto, searchSourceBuilder: SearchSourceBuilder) {
        if (searchRequestDto.page != null)
            searchSourceBuilder.from((searchRequestDto.page!! - 1) * searchRequestDto.pageSize!!)
        if (searchRequestDto.pageSize != null)
            searchSourceBuilder.size(searchRequestDto.pageSize!!)
    }
}