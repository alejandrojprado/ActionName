package com.mahisoft.kamino.mssearch.service.search.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "user_search")
data class UserSearchEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "search_name")
    var searchName: String,

    @Column(name = "dto")
    var dto: String,

    @Column(name = "user_id")
    var userId: Long
)