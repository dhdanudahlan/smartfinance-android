package com.aetherized.smartfinance.core.database.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.aetherized.smartfinance.core.utils.CategoryType

@Entity (
    tableName = "categories",
    indices = [
        Index(value = ["last_modified"]),
        Index(value = ["is_deleted"])
    ]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val type: CategoryType, // EXPENSE or INCOME
    val color: String? = null,
    val icon: String? = null,
    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false,
    @ColumnInfo(name = "last_modified")
    val lastModified: Long = System.currentTimeMillis(),
) {
    init {
        require(name.isNotBlank()) { "Category name cannot be empty." }
    }
}
