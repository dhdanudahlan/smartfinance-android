package com.aetherized.smartfinance.core.database.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.aetherized.smartfinance.core.utils.CategoryType
import com.aetherized.smartfinance.core.utils.CategoryValidator
import com.aetherized.smartfinance.features.categories.domain.model.Category
import java.time.Instant
import java.time.ZoneId

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
    val color: String? = null, // HEX Color Code
    val icon: String? = null, // URI or Resource Name
    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false,
    @ColumnInfo(name = "last_modified")
    val lastModified: Long = currentTimestamp,
) {
    companion object {
        val currentTimestamp: Long
            get() = System.currentTimeMillis()
    }

    fun toDomainModel(): Category {
        return Category(
            id = this.id,
            name = this.name,
            type = this.type,
            color = this.color,
            icon = this.icon,
            isDeleted = this.isDeleted,
            lastModified = Instant.ofEpochMilli(this.lastModified).atZone(ZoneId.systemDefault()).toLocalDateTime()
        )
    }
}
