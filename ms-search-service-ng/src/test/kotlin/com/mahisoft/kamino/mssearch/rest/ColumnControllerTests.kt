package com.mahisoft.kamino.mssearch.rest

import com.mahisoft.kamino.commons.exception.ApiHttpClientErrorException
import com.mahisoft.kamino.mssearch.BaseTest
import com.mahisoft.kamino.mssearch.ColumnClient
import com.mahisoft.kamino.mssearch.dto.UserColumnRequest
import com.mahisoft.kamino.mssearch.service.search.domain.IndexColumnEntity
import com.mahisoft.kamino.mssearch.service.search.domain.UserColumnEntity
import com.mahisoft.kamino.mssearch.service.search.repository.IndexColumnRepository
import com.mahisoft.kamino.mssearch.service.search.repository.UserColumnRepository
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.RestTemplate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@ContextConfiguration(initializers = [BaseTest.Companion.Initializer::class])
class ColumnControllerTests : BaseTest() {

    @Value("\${server.servlet.context-path}")
    lateinit var contextPath: String

    @Autowired
    lateinit var restTemplate: RestTemplate

    @Autowired
    lateinit var indexColumnRepository: IndexColumnRepository

    @Autowired
    lateinit var userColumnRepository: UserColumnRepository

    private lateinit var columnClient: ColumnClient

    @LocalServerPort
    var port: Int = 0

    private val baseUrl: String
        get() = "http://localhost:$port$contextPath"

    @BeforeAll
    fun setUp() {
        columnClient = ColumnClient(baseUrl, restTemplate)
        indexColumnRepository.deleteAll()
    }

    @AfterAll
    fun destroy() {
    }

    @Test
    fun `Get all available columns`() {
        addBaseColumns()
        val result = columnClient.getAllColumns()
        assertEquals(5, result.columns.size)
        assertFalse(result.columns.any {
            it.columnCode == "network"
        })
    }

    @Test
    fun `Get all available columns with all inactive columns`() {
        indexColumnRepository.deleteAll()
        indexColumnRepository.save(IndexColumnEntity(
            id = 1,
            columnName = "Message",
            columnCode = "message",
            active = false,
            isDisplayColumn = false,
            isFilterColumn = false
        ))
        indexColumnRepository.save(IndexColumnEntity(
            id = 2,
            columnName = "Group Id",
            columnCode = "group_id",
            active = false,
            isDisplayColumn = false,
            isFilterColumn = false))
        val result = columnClient.getAllColumns()
        assertTrue(result.columns.isEmpty())
    }

    @Test
    fun `Test add user column`() {
        addBaseColumns()
        columnClient.addUserColumn(UserColumnRequest("message"))
        columnClient.addUserColumn(UserColumnRequest("group_id"))
        columnClient.addUserColumn(UserColumnRequest("uuid"))
        val result = userColumnRepository.findAll()
        assertTrue(result.isNotEmpty())
        assertEquals(3, result.size)
    }

    @Test
    fun `Test add user column with already existing relation`() {
        addBaseColumns()
        columnClient.addUserColumn(UserColumnRequest("message"))
        columnClient.addUserColumn(UserColumnRequest("group_id"))
        assertThrows<ApiHttpClientErrorException> {
            columnClient.addUserColumn(UserColumnRequest("message"))
        }
        val result = userColumnRepository.findAll()
        assertTrue(result.isNotEmpty())
        assertEquals(2, result.size)
    }

    @Test
    fun `Test add user column with nonexistent column code`() {
        addBaseColumns()
        columnClient.addUserColumn(UserColumnRequest("group_id"))
        assertThrows<ApiHttpClientErrorException> {
            columnClient.addUserColumn(UserColumnRequest("nonexisten_code"))
        }
        val result = userColumnRepository.findAll()
        assertTrue(result.isNotEmpty())
        assertEquals(1, result.size)
    }

    @Test
    fun `Test delete user column`() {
        addBaseColumns()
        userColumnRepository.save(UserColumnEntity(
                columnId = indexColumnRepository.findByColumnCode("message").id!!,
                userId = 1
        ))
        userColumnRepository.save(UserColumnEntity(
                columnId = indexColumnRepository.findByColumnCode("group_id").id!!,
                userId = 1
        ))
        userColumnRepository.save(UserColumnEntity(
                columnId = indexColumnRepository.findByColumnCode("uuid").id!!,
                userId = 1
        ))
        columnClient.deleteUserColumn(UserColumnRequest("message"))
        columnClient.deleteUserColumn(UserColumnRequest("group_id"))
        columnClient.deleteUserColumn(UserColumnRequest("uuid"))
        val result = userColumnRepository.findAll()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `Test delete user column with nonexistent column`() {
        assertThrows<ApiHttpClientErrorException> {
            columnClient.deleteUserColumn(UserColumnRequest("nonexistent_column"))
        }
    }

    @Test
    fun `Test delete user column with nonexistent column-user relation`() {
        addBaseColumns()
        userColumnRepository.save(UserColumnEntity(
                columnId = indexColumnRepository.findByColumnCode("message").id!!,
                userId = 1
        ))
        assertThrows<ApiHttpClientErrorException> {
            columnClient.deleteUserColumn(UserColumnRequest("group_id"))
        }
    }

    @Test
    fun `Test get user columns`() {
        addBaseColumns()
        userColumnRepository.save(UserColumnEntity(
                columnId = indexColumnRepository.findByColumnCode("message").id!!,
                userId = 1
        ))
        val result = columnClient.getUserColumns()
        assertTrue(result.columns.isNotEmpty())
        assertEquals(1, result.columns.size)
    }

    private fun addBaseColumns() {
        indexColumnRepository.deleteAll()
        indexColumnRepository.save(IndexColumnEntity(
            id = 1, columnName = "Message",
            columnCode = "message",
            active = true,
            isFilterColumn = false,
            isDisplayColumn = false
        ))
        indexColumnRepository.save(IndexColumnEntity(
            id = 2,
            columnName = "Group Id",
            columnCode = "group_id",
            active = true,
            isFilterColumn = false,
            isDisplayColumn = false
        ))
        indexColumnRepository.save(IndexColumnEntity(
            id = 3,
            columnName = "UUID",
            columnCode = "uuid",
            active = true,
            isFilterColumn = false,
            isDisplayColumn = false
        ))
        indexColumnRepository.save(IndexColumnEntity(
            id = 4,
            columnName = "Network",
            columnCode = "network",
            active = false,
            isFilterColumn = false,
            isDisplayColumn = false
        ))
        indexColumnRepository.save(IndexColumnEntity(
            id = 5,
            columnName = "Channel",
            columnCode = "channel",
            active = true,
            isFilterColumn = true,
            isDisplayColumn = false
        ))
        indexColumnRepository.save(IndexColumnEntity(
            id = 5,
            columnName = "Country",
            columnCode = "country",
            active = true,
            isFilterColumn = false,
            isDisplayColumn = true
        ))
    }

    @Test
    fun `Get filter columns`() {
        addBaseColumns()
        val result = columnClient.getFilterColumns()
        assertEquals(1, result.columns.size)
        assertEquals("channel", result.columns.first().columnCode)
    }

    @Test
    fun `Get display columns`() {
        addBaseColumns()
        val result = columnClient.getDisplayColumns()
        assertEquals(1, result.columns.size)
        assertEquals("country", result.columns.first().columnCode)
    }
}