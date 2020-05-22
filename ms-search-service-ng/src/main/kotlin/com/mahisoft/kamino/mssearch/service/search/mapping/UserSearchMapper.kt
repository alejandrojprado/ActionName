package com.mahisoft.kamino.mssearch.service.search.mapping

import com.mahisoft.kamino.mssearch.dto.UserSearch
import com.mahisoft.kamino.mssearch.service.search.domain.UserSearchEntity

fun toDto(userSearchEntity: UserSearchEntity): UserSearch =
        UserSearch(
                id = userSearchEntity.id,
                searchName = userSearchEntity.searchName,
                dto = userSearchEntity.dto,
                userId = userSearchEntity.userId
        )

fun toEntity(userSearch: UserSearch): UserSearchEntity =
        UserSearchEntity(
                id = userSearch.id,
                searchName = userSearch.searchName,
                dto = userSearch.dto,
                userId = userSearch.userId
        )
