package com.aetherized.smartfinance.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aetherized.smartfinance.core.database.entity.CategoryEntity
import com.aetherized.smartfinance.core.utils.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCategory(category: CategoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAllCategories(entities: List<CategoryEntity>)

    // Soft delete: mark record as deleted
    @Query("UPDATE categories SET is_deleted = 1, last_modified = :timestamp, sync_status = :syncStatus WHERE id = :id")
    suspend fun softDeleteCategoryById(id: Long, timestamp: Long = System.currentTimeMillis(), syncStatus: String = SyncStatus.PENDING.name)

    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    suspend fun getCategoryById(id: Long): CategoryEntity?

    @Query("SELECT * FROM categories WHERE is_deleted = 0 ORDER BY last_modified DESC LIMIT :limit OFFSET :offset")
    fun getAllActiveCategories(limit: Int = 50, offset: Int = 0): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE type = :type AND is_deleted = 0 ORDER BY last_modified DESC LIMIT :limit OFFSET :offset")
    fun getCategoriesByType(type: String, limit: Int = 50, offset: Int = 0): Flow<List<CategoryEntity>>
}

