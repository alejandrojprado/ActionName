package com.mahisoft.kamino.mssearch

import com.mahisoft.kamino.mssearch.containers.CustomElasticSearchContainer
import com.mahisoft.kamino.mssearch.containers.CustomMysqlContainer
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.indices.CreateIndexRequest
import org.elasticsearch.common.xcontent.XContentType
import org.junit.ClassRule
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.util.ResourceUtils
import java.io.File
import java.nio.file.Files
import java.time.Instant

open class BaseTest {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val logger = LoggerFactory.getLogger(javaClass.enclosingClass)

        @ClassRule
        var mysql: CustomMysqlContainer = CustomMysqlContainer.getInstance()

        @ClassRule
        var elasticSearch: CustomElasticSearchContainer = CustomElasticSearchContainer.getInstance()

        const val INDEX_NAME = "messages"

        class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(applicationContext: ConfigurableApplicationContext) {
                mysql.start()
                TestPropertyValues.of(
                        "spring.datasource.jdbc-url=" + mysql.jdbcUrl,
                        "spring.datasource.username=" + mysql.username,
                        "spring.datasource.password=" + mysql.password
                ).applyTo(applicationContext.environment)

                elasticSearch.start()
            }
        }
    }

    @Autowired
    lateinit var client: RestHighLevelClient

    var mappingsFile: File = ResourceUtils.getFile("classpath:mappings/messages.json")
    var settingsFile: File = ResourceUtils.getFile("classpath:mappings/settings.json")
    fun createIndex() {
        val request = CreateIndexRequest(INDEX_NAME)
        val mappings = String(Files.readAllBytes(mappingsFile.toPath()))
        val settings = String(Files.readAllBytes(settingsFile.toPath()))
        request.mapping(mappings, XContentType.JSON)
        request.settings(settings, XContentType.JSON)
        client.indices().create(request, RequestOptions.DEFAULT)
    }

    fun buildDoc(
        groupId: String,
        message: String,
        messageId: Long,
        username: String? = null,
        network: String? = null,
        imageDate: Instant? = null
    ): MutableMap<String, Any> {
        val jsonMap: MutableMap<String, Any> = HashMap()
        jsonMap["group_id"] = groupId
        jsonMap["message"] = message
        jsonMap["message_id"] = messageId
        username?.let {
            jsonMap["user_name"] = it
        }
        network?.let {
            jsonMap["network"] = it
        }
        imageDate?.let {
            jsonMap["image_date"] = it
        }
        return jsonMap
    }

    fun insertData() {
        createDocument("1", buildDoc("123456", "The house is red!", 1))
        createDocument("2", buildDoc("123457", "The fox is brown.", 2))
        createDocument("3", buildDoc("123458", "The best burger in NY", 3, "eperez88"))
        createDocument("4", buildDoc("123459", "The sky is blue.", 4, network = "IRC"))
        createDocument("5", buildDoc("123460", "The skyline is not blue is red.", 5, network = "Telegram"))

        createDocument("6", buildDoc("123461", "Today is a good day.", 6, imageDate =
        Instant.parse("2020-03-15T00:00:00.000Z")))
        createDocument("7", buildDoc("123462", "Tomorrow is even better.", 7, username = "eperez1",
                imageDate = Instant.parse("2020-03-25T00:00:00.000Z")))
        createDocument("8", buildDoc("123463", "Hey! The Silver back gorilla is there", 8))
        createDocument("9", buildDoc("123464", "07OH Scott Pilgrim Vs the World is on netflix :D", 9))
        createDocument("10", buildDoc("123465", "containedThis is a cool testyeah what's up", 10))
    }

    fun createDocument(id: String, data: MutableMap<String, Any>) {
        val request = IndexRequest(INDEX_NAME)
        request.id(id).type("_doc")
        request.source(data, XContentType.JSON)
        client.index(request, RequestOptions.DEFAULT)
    }

    fun deleteIndex() {
        val request = DeleteIndexRequest(INDEX_NAME)
        client.indices().delete(request, RequestOptions.DEFAULT)
    }
}