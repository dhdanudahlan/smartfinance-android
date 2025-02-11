package com.aetherized.smartfinance.core.utils

import com.aetherized.smartfinance.core.database.entity.CategoryEntity
import com.aetherized.smartfinance.core.database.entity.TransactionEntity
import com.aetherized.smartfinance.features.finance.data.dto.CategoryDto
import com.aetherized.smartfinance.features.finance.data.dto.TransactionDto
import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.Transaction
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Locale


/** -------------------------
Transaction
----------------------------*/
fun Transaction.toEntity(): TransactionEntity {
    if (id != (-1).toLong()) {
        return TransactionEntity(
            id = this.id,
            categoryId = this.categoryId,
            amount = this.amount,
            note = this.note,
            timestamp = this.timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            isDeleted = this.isDeleted,
            lastModified = this.lastModified.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
    } else {
        return TransactionEntity(
            categoryId = this.categoryId,
            amount = this.amount,
            note = this.note,
            timestamp = this.timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            isDeleted = this.isDeleted,
            lastModified = this.lastModified.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
    }
}

fun Transaction.toDto(): TransactionDto {
    return TransactionDto(
        id = this.id,
        categoryId = this.categoryId,
        amount = this.amount,
        note = this.note,
        timestamp = this.timestamp.toString(),
        isDeleted = this.isDeleted,
        lastModified = this.lastModified.toString()
    )
}


fun TransactionDto.toDomainModel(): Transaction {
    return Transaction(
        id = this.id,
        categoryId = this.categoryId,
        amount = this.amount,
        note = this.note,
        timestamp = LocalDateTime.parse(this.timestamp),
        isDeleted = this.isDeleted,
        lastModified = LocalDateTime.parse(this.lastModified)
    )
}

fun TransactionEntity.toDomainModel(): Transaction {
    return Transaction(
        id = id,
        categoryId = categoryId,
        amount = amount,
        note = note,
        timestamp = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime(),
        isDeleted = isDeleted,
        lastModified = Instant.ofEpochMilli(lastModified).atZone(ZoneId.systemDefault()).toLocalDateTime()
    )
}


/** -------------------------
Category
----------------------------*/
fun Category.toEntity(): CategoryEntity {
    if (id != (-1).toLong()) {
        return CategoryEntity(
            id = this.id,
            name = this.name,
            type = this.type,
            color = this.color,
            icon = this.icon,
            isDeleted = this.isDeleted,
            lastModified = this.lastModified.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
    } else {
        return CategoryEntity(
            name = this.name,
            type = this.type,
            color = this.color,
            icon = this.icon,
            isDeleted = this.isDeleted,
            lastModified = this.lastModified.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
    }
}

fun Category.toDto(): CategoryDto {
    return CategoryDto(
        id = this.id,
        name = this.name,
        type = this.type,
        color = this.color,
        icon = this.icon,
        isDeleted = this.isDeleted,
        lastModified = this.lastModified.toString()
    )
}

fun CategoryEntity.toDomainModel(): Category {
    return Category(
        id = id,
        name = name,
        type = type,
        color = color,
        icon = icon,
        isDeleted = isDeleted,
        lastModified = Instant.ofEpochMilli(lastModified).atZone(ZoneId.systemDefault()).toLocalDateTime()
    )
}

fun CategoryDto.toDomainModel(): Category {
    return Category(
        id = this.id,
        name = this.name,
        type = this.type,
        color = this.color,
        icon = this.icon,
        isDeleted = this.isDeleted,
        lastModified = LocalDateTime.parse(this.lastModified)
    )
}



/** -------------------------
PRIMARY: Int
----------------------------*/
fun Int.toCommaSeparatedString(): String {
    val numberFormat = NumberFormat.getNumberInstance(Locale.US)
    return numberFormat.format(this)
}
fun Int.toRupiahString(): String {
    return "Rp ${this.toCommaSeparatedString()}"
}
