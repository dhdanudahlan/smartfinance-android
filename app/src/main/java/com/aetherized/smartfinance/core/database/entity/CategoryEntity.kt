package com.aetherized.smartfinance.core.database.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.aetherized.smartfinance.core.utils.SyncStatus
import com.aetherized.smartfinance.features.records.domain.model.Category
import com.aetherized.smartfinance.features.records.domain.model.CategoryType
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
    val type: CategoryType, // e.g., EXPENSE or INCOME
    val color: String? = null, // HEX Color Code
    val icon: String? = null, // URI or Resource Name
    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean = false,
    @ColumnInfo(name = "last_modified")
    val lastModified: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "sync_status")
    val syncStatus: SyncStatus = SyncStatus.PENDING
) {
    fun toDomainModel(): Category {
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
}
