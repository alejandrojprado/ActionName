package com.mahisoft.kamino.mssearch.rest

import com.mahisoft.kamino.commons.rest.http.Constants
import com.mahisoft.kamino.mssearch.dto.IndexColumnResponse
import com.mahisoft.kamino.mssearch.dto.UserColumnRequest
import com.mahisoft.kamino.mssearch.service.ColumnService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@Controller
@RequestMapping(Constants.V1 + "/column")
@Api(value = Constants.V1 + "/column", description = "Column API")
class ColumnController(private val columnService: ColumnService) {
    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        private val logger = LoggerFactory.getLogger(javaClass.enclosingClass)
    }

    @ApiOperation(value = "Return the columns that are shown in the filter dropdown.")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Request successful"),
        ApiResponse(code = 400, message = "Validation failed, the dto is not compatible"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = [RequestMethod.GET], value = ["/filter"])
    @ResponseBody
    fun getFilterColumns(): IndexColumnResponse = columnService.getFilterColumns()

    @ApiOperation(value = "Return all the columns that are active.")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Request successful"),
        ApiResponse(code = 400, message = "Validation failed, the dto is not compatible"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = [RequestMethod.GET], value = ["/all"])
    @ResponseBody
    fun getAllColumns(): IndexColumnResponse = columnService.getActiveColumns()

    @ApiOperation(value = "Return the columns that are shown in the + Add Column dropdown.")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Request successful"),
        ApiResponse(code = 400, message = "Validation failed, the dto is not compatible"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = [RequestMethod.GET], value = ["/display"])
    @ResponseBody
    fun getDisplayColumns(): IndexColumnResponse = columnService.getDisplayColumns()

    @ApiOperation(value = "Get all user selected columns to filter search results.")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Request successful"),
        ApiResponse(code = 400, message = "Validation failed, the dto is not compatible"),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = [RequestMethod.GET], value = ["/user/"])
    @ResponseBody
    fun getUserColumns(): IndexColumnResponse = columnService.getUserColumns()

    @ApiOperation(value = "Add a column to a user.")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Created successfully"),
        ApiResponse(code = 404, message = "The column id does not exists."),
        ApiResponse(code = 409, message = "The column is already related to the user."),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = [RequestMethod.POST], value = ["/user/"])
    @ResponseBody
    fun addUserColumn(
        @RequestBody
        @Validated
        @ApiParam(name = "User Column Request", value = "Used for user/column related operations.")
        userColumnRequest: UserColumnRequest
    ) = columnService.addUserColumn(userColumnRequest)

    @ApiOperation(value = "Delete a user column relation.")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Created successfully"),
        ApiResponse(code = 404, message = "The column id does not exists."),
        ApiResponse(code = 404, message = "This column user/relation does not exists."),
        ApiResponse(code = 500, message = "Internal server error")
    ])
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = [RequestMethod.DELETE], value = ["/user/"])
    @ResponseBody
    fun deleteUserColumn(
        @RequestBody
        @Validated
        @ApiParam(name = "User Column Request", value = "Used for user/column related operations.")
        userColumnRequest: UserColumnRequest
    ) = columnService.deleteUserColumn(userColumnRequest)
}