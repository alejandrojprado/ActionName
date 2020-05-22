package com.mahisoft.kamino.mssearch.rest

import com.mahisoft.kamino.mssearch.BaseTest
import com.mahisoft.kamino.mssearch.SearchClient
import com.mahisoft.kamino.mssearch.dto.ResultPage
import com.mahisoft.kamino.mssearch.dto.SearchRequestDto
import com.mahisoft.kamino.mssearch.dto.Sort
import com.mahisoft.kamino.mssearch.dto.filters.CustomSortOrder
import com.mahisoft.kamino.mssearch.dto.filters.Filter
import com.mahisoft.kamino.mssearch.dto.filters.FilterType
import com.mahisoft.kamino.mssearch.dto.filters.MatchType
import com.mahisoft.kamino.mssearch.dto.filters.RangeFilter
import com.mahisoft.kamino.mssearch.containers.CustomElasticSearchContainer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.RestTemplate
import java.time.Instant

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@ContextConfiguration(initializers = [BaseTest.Companion.Initializer::class])
class SearchControllerTests : BaseTest() {

    @Value("\${server.servlet.context-path}")
    lateinit var contextPath: String

    @Autowired
    lateinit var restTemplate: RestTemplate

    private lateinit var searchClient: SearchClient

    private lateinit var elasticsearchContainer: CustomElasticSearchContainer

    @LocalServerPort
    var port: Int = 0

    private val baseUrl: String
        get() = "http://localhost:$port$contextPath"

    @BeforeAll
    fun setUp() {
        searchClient = SearchClient(baseUrl, restTemplate)
        createIndex()
        insertData()
    }

    @AfterAll
    fun destroy() {
        deleteIndex()
    }

    @Test
    fun `Test search exact`() {
        val result = buildTextSearchRequest(
                "message",
                listOf("group_id", "message"),
                listOf(MatchType.EXACT),
                listOf("house"))
        assertEquals(1, result.total)
        assertEquals("123456", result.results.first().groupId)
    }

    @Test
    fun `Test search fuzzy`() {
        val result = buildTextSearchRequest(
                "message",
                listOf("group_id", "message"),
                listOf(MatchType.FUZZY),
                listOf("housa"))
        assertEquals(1, result.total)
        assertEquals("123456", result.results.first().groupId)
    }

    @Test
    fun `Test search contained`() {
        val result = buildTextSearchRequest(
                "message",
                listOf("group_id", "message"),
                listOf(MatchType.CONTAINED),
                listOf("fox"))
        assertEquals(1, result.total)
        assertEquals("123457", result.results.first().groupId)
    }

    @Test
    fun `Test search contained with multiple words`() {
        val result = buildTextSearchRequest(
                "message",
                listOf("group_id", "message", "message_id"),
                listOf(MatchType.CONTAINED),
                listOf("cool test"))
        assertEquals(1, result.total)
        assertEquals(10, result.results.first().messageId)
    }

    @Test
    fun `Test combined search`() {
        val filter1 = Filter("message", FilterType.TEXT, listOf(MatchType.EXACT), listOf("burger"))
        val filter2 = Filter("user_name", FilterType.TEXT, listOf(MatchType.EXACT), listOf("eperez88"))
        val filters = listOf(filter1, filter2)
        val searchRequest = SearchRequestDto(filters, listOf("group_id", "message", "user_name"), 1, 10)
        val result = searchClient.search(searchRequest)
        assertEquals(1, result.total)
        assertEquals("123458", result.results.first().groupId)
    }

    @Test
    fun `Test range search`() {
        val filters = buildRangeFilters()
        var searchRequest = SearchRequestDto(filters, listOf("group_id", "message", "user_name"), 1, 10,
                Sort("image_date", CustomSortOrder.ASC)
        )
        var result = searchClient.search(searchRequest)
        assertEquals(2, result.total)
        assertEquals("123461", result.results[0].groupId)
        assertEquals("123462", result.results[1].groupId)
        searchRequest = SearchRequestDto(filters, listOf("group_id", "message", "user_name"), 1, 10,
                Sort("image_date", CustomSortOrder.DESC)
        )
        result = searchClient.search(searchRequest)
        assertEquals("123462", result.results[0].groupId)
        assertEquals("123461", result.results[1].groupId)
    }

    private fun buildRangeFilters(): List<Filter> {
        val rangeFilter = RangeFilter(
                from = Instant.parse("2020-03-01T00:00:00.000Z"),
                to = Instant.parse("2020-03-30T00:00:00.000Z")
        )
        val filter1 = Filter("image_date", FilterType.RANGE, rangeValues = listOf(rangeFilter))
        val filters = listOf(filter1)
        return filters
    }

    @Test
    fun `Test combined search with repeating filters`() {
        val filter1 = Filter("message", FilterType.TEXT, listOf(MatchType.EXACT), listOf("blue"))
        val filter2 = Filter("network", FilterType.TEXT, listOf(MatchType.EXACT, MatchType.EXACT), listOf("IRC", "Telegram"))
        val filters = listOf(filter1, filter2)
        val searchRequest = SearchRequestDto(filters, listOf("group_id", "message", "network"), 1, 10,
                Sort("message_id", CustomSortOrder.ASC))
        val result = searchClient.search(searchRequest)
        assertEquals(2, result.total)
        assertEquals("123459", result.results[0].groupId)
        assertEquals("123460", result.results[1].groupId)
    }

    private fun buildTextSearchRequest(
        key: String,
        columns: List<String>,
        matchType: List<MatchType>,
        values: List<String>
    ): ResultPage {
        val filter1 = Filter(key, FilterType.TEXT, matchType, values)
        val filters = listOf(filter1)
        val searchRequest = SearchRequestDto(filters, columns, 1, 10)
        return searchClient.search(searchRequest)
    }

    @Test
    fun `Test exact search with multiple words`() {
        val result = buildTextSearchRequest(
                "message",
                listOf("group_id", "message", "message_id"),
                listOf(MatchType.EXACT),
                listOf("Silver back gorilla"))
        assertEquals(1, result.total)
        assertEquals(8, result.results.first().messageId)
    }

    @Test
    fun `Test exact search with multiple words case insensitive`() {
        val result = buildTextSearchRequest(
                "message",
                listOf("group_id", "message", "message_id"),
                listOf(MatchType.EXACT),
                listOf("sIlver back GorilLa"))
        assertEquals(1, result.total)
        assertEquals(8, result.results.first().messageId)
    }

    @Test
    fun `Test search fuzzy with multiple words`() {
        val result = buildTextSearchRequest(
                "message",
                listOf("group_id", "message", "message_id"),
                listOf(MatchType.FUZZY),
                listOf("scottyy pilgrimii"))
        assertEquals(1, result.total)
        assertEquals(9, result.results.first().messageId)
    }

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val logger = LoggerFactory.getLogger(javaClass.enclosingClass)
        const val ELASTIC_PORT = 9200
    }
}