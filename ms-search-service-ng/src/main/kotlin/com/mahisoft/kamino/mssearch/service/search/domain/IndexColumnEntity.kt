package com.mahisoft.kamino.mssearch.service.search.domain

import org.hibernate.annotations.Type
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "index_column")
data class IndexColumnEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "column_code")
    var columnCode: String,

    @Column(name = "column_name")
    var columnName: String,

    @Column(name = "active", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var active: Boolean,

    @Column(name = "is_filter_column", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var isFilterColumn: Boolean,

    @Column(name = "is_display_column", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    var isDisplayColumn: Boolean
)