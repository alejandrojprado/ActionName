package com.mahisoft.kamino.mssearch.service.search.mapping

import com.mahisoft.kamino.mssearch.dto.IndexColumn
import com.mahisoft.kamino.mssearch.service.search.domain.IndexColumnEntity

fun toDto(indexColumnEntity: IndexColumnEntity): IndexColumn =
        IndexColumn(
            columnCode = indexColumnEntity.columnCode,
            columnName = indexColumnEntity.columnName
        )
