package com.mahisoft.kamino.mssearch.service.search.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "user_column")
data class UserColumnEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "column_id")
    val columnId: Long,

    @Column(name = "user_id")
    var userId: Long
)