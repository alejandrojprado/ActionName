package com.mahisoft.kamino.mssearch.service

import com.mahisoft.kamino.mssearch.dto.IndexColumn
import com.mahisoft.kamino.mssearch.dto.IndexColumnResponse
import com.mahisoft.kamino.mssearch.dto.UserColumnRequest
import com.mahisoft.kamino.mssearch.exception.ExistentRelationException
import com.mahisoft.kamino.mssearch.service.search.domain.IndexColumnEntity
import com.mahisoft.kamino.mssearch.service.search.domain.UserColumnEntity
import com.mahisoft.kamino.mssearch.service.search.mapping.toDto
import com.mahisoft.kamino.mssearch.service.search.repository.IndexColumnRepository
import com.mahisoft.kamino.mssearch.service.search.repository.UserColumnRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

/**
 * Service to perform queries over elastic search index
 */
@Service
class ColumnService(
    val indexColumnRepository: IndexColumnRepository,
    val userColumnRepository: UserColumnRepository
) {

    companion object {
        const val DEFAULT_MINUS_DAYS: Long = 7
        const val SINGLE_VALUE = 1

        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val logger = LoggerFactory.getLogger(javaClass.enclosingClass)
    }

    /**
     * Called to get all active index columns
     *
     * @return a list of IndexColumn
     */
    fun getActiveColumns(): IndexColumnResponse {
        return getColumnsResult(indexColumnRepository.findActiveColumns())
    }

    /**
     * Called to get all active index columns
     *
     * @return a list of IndexColumn
     */
    fun getFilterColumns(): IndexColumnResponse {
        return getColumnsResult(indexColumnRepository.findFilterColumns())
    }

    /**
     * Called to get all active index columns
     *
     * @return a list of IndexColumn
     */
    fun getDisplayColumns(): IndexColumnResponse {
        return getColumnsResult(indexColumnRepository.findDisplayColumns())
    }

    /**
     * Called to get the selected columns by a user to filter the search results
     *
     * @return a list of IndexColumn
     */
    // TODO: Figure out how to get the real userId
    fun getUserColumns(): IndexColumnResponse {
        val userId: Long = 1
        return getColumnsResult(indexColumnRepository.findUserColumns(userId))
    }

    /**
     * Called to map all the index column entities to a index column dto
     *
     * @param indexColumnEntityList a list of IndexColumnEntity
     *
     * @return a list if index column dto
     */
    private fun getColumnsResult(indexColumnEntityList: List<IndexColumnEntity>): IndexColumnResponse {
        val resultList = arrayListOf<IndexColumn>()
        for (indexColumnEntity in indexColumnEntityList) {
            resultList.add(toDto(indexColumnEntity))
        }
        return IndexColumnResponse(columns = resultList)
    }

    /**
     * Called to add a column/user relation
     *
     * @param userColumnRequest a object with the columnId
     *
     * @throws EntityNotFoundException in case the specified columns don't exists.
     * @throws ExistentRelationException in case the column/user relation already exists.
     *
     */
    // TODO: Figure out how to get the real userId
    fun addUserColumn(userColumnRequest: UserColumnRequest) {
        val userId: Long = 1
        val columnCode = userColumnRequest.columnCode!!
        if (!indexColumnRepository.existsByColumnCode(columnCode))
            throw EntityNotFoundException("The column id does not exists.")
        val indexColumn = indexColumnRepository.findByColumnCode(columnCode)
        val columnId = indexColumn.id!!
        if (userColumnRepository.existsByColumnIdAndUserId(columnId, userId))
            throw ExistentRelationException("The column is already related to the user.")
        val userColumnEntity = UserColumnEntity(columnId = columnId, userId = userId)
        userColumnRepository.save(userColumnEntity)
    }

    /**
     * Called to delete a column/user relation
     *
     * @param userColumnRequest with the columnId
     *
     * @throws EntityNotFoundException in case the specified columns don't exists or the column/user relation don't exists.
     */
    // TODO: Figure out how to get the real userId
    fun deleteUserColumn(userColumnRequest: UserColumnRequest) {
        val userId: Long = 1
        val columnCode = userColumnRequest.columnCode!!
        if (!indexColumnRepository.existsByColumnCode(columnCode))
            throw EntityNotFoundException("The column id does not exists.")
        val indexColumn = indexColumnRepository.findByColumnCode(columnCode)
        val columnId = indexColumn.id!!
        if (!userColumnRepository.existsByColumnIdAndUserId(columnId, userId))
            throw EntityNotFoundException("This column user/relation does not exists.")
        val userColumnEntity = userColumnRepository.findByColumnIdAndUserId(columnId, userId)
        userColumnRepository.delete(userColumnEntity)
    }
}