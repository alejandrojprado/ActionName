package com.mahisoft.kamino.mssearch.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.mahisoft.kamino.mssearch.BaseTest
import com.mahisoft.kamino.mssearch.config.ElasticSearchColumnsConfiguration
import com.mahisoft.kamino.mssearch.dto.filters.Filter
import com.mahisoft.kamino.mssearch.dto.filters.FilterType
import com.mahisoft.kamino.mssearch.dto.filters.MatchType
import com.mahisoft.kamino.mssearch.dto.filters.RangeFilter
import com.mahisoft.kamino.mssearch.exception.CustomException
import org.elasticsearch.client.RestHighLevelClient
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@ContextConfiguration(initializers = [BaseTest.Companion.Initializer::class])
class SearchServiceTest : BaseTest() {

    var searchService: SearchService? = null

    @Autowired
    lateinit var elasticSearchColumnsConfiguration: ElasticSearchColumnsConfiguration

    @BeforeAll
    fun setup() {
        val client = mock(RestHighLevelClient::class.java)
        val objectMapper = mock(ObjectMapper::class.java)
        searchService = SearchService(client, objectMapper, elasticSearchColumnsConfiguration)
    }

    @Test
    fun `Test addTextQuery with a single value`() {
        val filter = Filter("message", FilterType.TEXT, values = listOf("house"), matchType = listOf(MatchType.EXACT))
        val queryBuilder = searchService!!.addTextQuery(filter)
        assertEquals("""
            {
              "match" : {
                "message" : {
                  "query" : "house",
                  "operator" : "AND",
                  "prefix_length" : 0,
                  "max_expansions" : 50,
                  "fuzzy_transpositions" : true,
                  "lenient" : false,
                  "zero_terms_query" : "NONE",
                  "auto_generate_synonyms_phrase_query" : true,
                  "boost" : 1.0
                }
              }
            }
        """.trimIndent(), queryBuilder.toString())
    }

    @Test
    fun `Test addTextQuery with wrong number of arguments in match type`() {
        assertThrows<CustomException> {
            val filter = Filter("message", FilterType.TEXT, values = listOf("house"))
            searchService!!.addTextQuery(filter)
        }
    }

    @Test
    fun `Test addTextQuery with empty values`() {
        val filter = Filter("message", FilterType.TEXT)
        assertNull(searchService!!.addTextQuery(filter))
    }

    @Test
    fun `Test addTextQuery with null values`() {
        val filter = Filter("message", FilterType.TEXT)
        assertNull(searchService!!.addTextQuery(filter))
    }

    @Test
    fun `Test addTextQuery with a multiple values`() {
        val filter = Filter(
                "message",
                FilterType.TEXT,
                values = listOf("house", "door"),
                matchType = listOf(MatchType.EXACT, MatchType.FUZZY))
        val queryBuilder = searchService!!.addTextQuery(filter)
        assertEquals("""
            {
              "bool" : {
                "should" : [
                  {
                    "match" : {
                      "message" : {
                        "query" : "house",
                        "operator" : "AND",
                        "prefix_length" : 0,
                        "max_expansions" : 50,
                        "fuzzy_transpositions" : true,
                        "lenient" : false,
                        "zero_terms_query" : "NONE",
                        "auto_generate_synonyms_phrase_query" : true,
                        "boost" : 1.0
                      }
                    }
                  },
                  {
                    "match" : {
                      "message" : {
                        "query" : "door",
                        "operator" : "AND",
                        "fuzziness" : "2",
                        "prefix_length" : 1,
                        "max_expansions" : 50,
                        "fuzzy_transpositions" : true,
                        "lenient" : false,
                        "zero_terms_query" : "NONE",
                        "auto_generate_synonyms_phrase_query" : true,
                        "boost" : 1.0
                      }
                    }
                  }
                ],
                "adjust_pure_negative" : true,
                "boost" : 1.0
              }
            }
        """.trimIndent(), queryBuilder.toString())
    }

    @Test
    fun `Test addRangeQuery with a single value`() {
        val from = Instant.parse("2020-04-25T00:00:00.000Z")
        val to = Instant.parse("2020-04-26T00:00:00.000Z")
        val rangeFilter = RangeFilter(from, to)
        val filter = Filter("date_field", FilterType.RANGE, rangeValues = listOf(rangeFilter))
        val queryBuilder = searchService!!.addRangeQuery(filter)
        assertEquals("""
            {
              "range" : {
                "date_field" : {
                  "from" : "2020-04-25T00:00:00.000Z",
                  "to" : "2020-04-26T00:00:00.000Z",
                  "include_lower" : true,
                  "include_upper" : true,
                  "boost" : 1.0
                }
              }
            }
        """.trimIndent(), queryBuilder.toString())
    }

    @Test
    fun `Test addRangeQuery with multiple values`() {
        val from1 = Instant.parse("2020-04-25T00:00:00.000Z")
        val to1 = Instant.parse("2020-04-26T00:00:00.000Z")
        val rangeFilter1 = RangeFilter(from1, to1)

        val from2 = Instant.parse("2020-04-21T00:00:00.000Z")
        val to2 = Instant.parse("2020-04-25T00:00:00.000Z")
        val rangeFilter2 = RangeFilter(from2, to2)

        val filter = Filter("date_field", FilterType.RANGE, rangeValues = listOf(rangeFilter1, rangeFilter2))
        val queryBuilder = searchService!!.addRangeQuery(filter)

        assertEquals("""
            {
              "bool" : {
                "should" : [
                  {
                    "range" : {
                      "date_field" : {
                        "from" : "2020-04-25T00:00:00.000Z",
                        "to" : "2020-04-26T00:00:00.000Z",
                        "include_lower" : true,
                        "include_upper" : true,
                        "boost" : 1.0
                      }
                    }
                  },
                  {
                    "range" : {
                      "date_field" : {
                        "from" : "2020-04-21T00:00:00.000Z",
                        "to" : "2020-04-25T00:00:00.000Z",
                        "include_lower" : true,
                        "include_upper" : true,
                        "boost" : 1.0
                      }
                    }
                  }
                ],
                "adjust_pure_negative" : true,
                "boost" : 1.0
              }
            }
        """.trimIndent(), queryBuilder.toString())
    }

    @Test
    fun `Test addRangeQuery with null values`() {
        val filter = Filter("date_field", FilterType.RANGE)
        val queryBuilder = searchService!!.addRangeQuery(filter)
        assertNull(queryBuilder)
    }

    @Test
    fun `Test addRangeQuery with empty values`() {
        val filter = Filter("date_field", FilterType.RANGE, rangeValues = emptyList())
        val queryBuilder = searchService!!.addRangeQuery(filter)
        assertNull(queryBuilder)
    }

    @Test
    fun `Test getRangeFilter`() {
        val from = Instant.parse("2020-04-25T00:00:00.000Z")
        val to = Instant.parse("2020-04-26T00:00:00.000Z")
        val rangeFilter = RangeFilter(from, to)

        val queryBuilder = searchService!!.getRangeFilter("date_field", rangeFilter)
        assertEquals("""
            {
              "range" : {
                "date_field" : {
                  "from" : "2020-04-25T00:00:00.000Z",
                  "to" : "2020-04-26T00:00:00.000Z",
                  "include_lower" : true,
                  "include_upper" : true,
                  "boost" : 1.0
                }
              }
            }
        """.trimIndent(), queryBuilder.toString())
    }

    @Test
    fun `Test getQueryByType`() {
        val exactQueryBuilder = searchService!!.getQueryByType("key", "value", MatchType.EXACT)
        assertEquals("""
            {
              "match" : {
                "key" : {
                  "query" : "value",
                  "operator" : "AND",
                  "prefix_length" : 0,
                  "max_expansions" : 50,
                  "fuzzy_transpositions" : true,
                  "lenient" : false,
                  "zero_terms_query" : "NONE",
                  "auto_generate_synonyms_phrase_query" : true,
                  "boost" : 1.0
                }
              }
            }
        """.trimIndent(), exactQueryBuilder.toString())
        val fuzzyQueryBuilder = searchService!!.getQueryByType("key", "value", MatchType.FUZZY)
        assertEquals("""
            {
              "match" : {
                "key" : {
                  "query" : "value",
                  "operator" : "AND",
                  "fuzziness" : "2",
                  "prefix_length" : 1,
                  "max_expansions" : 50,
                  "fuzzy_transpositions" : true,
                  "lenient" : false,
                  "zero_terms_query" : "NONE",
                  "auto_generate_synonyms_phrase_query" : true,
                  "boost" : 1.0
                }
              }
            }
        """.trimIndent(), fuzzyQueryBuilder.toString())
        val containedQueryBuilder = searchService!!.getQueryByType("key", "value", MatchType.CONTAINED)
        assertEquals("""
            {
              "match" : {
                "key" : {
                  "query" : "value",
                  "operator" : "AND",
                  "prefix_length" : 0,
                  "max_expansions" : 50,
                  "fuzzy_transpositions" : true,
                  "lenient" : false,
                  "zero_terms_query" : "NONE",
                  "auto_generate_synonyms_phrase_query" : true,
                  "boost" : 1.0
                }
              }
            }
        """.trimIndent(), containedQueryBuilder.toString())
    }

    @Test
    fun `Test getQueryByType contained`() {
        val containedQueryBuilder = searchService!!.getQueryByType("message", "value", MatchType.CONTAINED)
        assertEquals("""
            {
              "match" : {
                "message.contained" : {
                  "query" : "value",
                  "operator" : "AND",
                  "prefix_length" : 0,
                  "max_expansions" : 50,
                  "fuzzy_transpositions" : true,
                  "lenient" : false,
                  "zero_terms_query" : "NONE",
                  "auto_generate_synonyms_phrase_query" : true,
                  "boost" : 1.0
                }
              }
            }
        """.trimIndent(), containedQueryBuilder.toString())
    }
}