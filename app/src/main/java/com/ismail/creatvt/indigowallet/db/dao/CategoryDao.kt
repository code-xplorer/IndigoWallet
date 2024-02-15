package com.ismail.creatvt.indigowallet.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ismail.creatvt.indigowallet.db.entity.Category

@Dao
interface CategoryDao {

    @Insert
    suspend fun insert(category: Category)

    @Insert
    suspend fun insertAll(category: List<Category>)

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query("select count(category_id) from category")
    suspend fun getCount():Int

    @Query("select count(category_id) from category where type = :type")
    fun getCountForType(type:Int):LiveData<Int>

    @Query("select * from category where category_id=:id")
    fun getCategoryForId(id: Int):LiveData<Category>

    @Query("select * from category where type = 0")
    fun getIncomeCategories(): LiveData<List<Category>>

    @Query("select * from category")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("select * from category")
    suspend fun getAllCategoriesSnapshot(): List<Category>

    @Query("select * from category where type = 1")
    fun getExpenseCategories(): LiveData<List<Category>>

    @Query("select * from category where type = 0")
    suspend fun getIncomeCategoriesSnapshot(): List<Category>

    @Query("select * from category where type = 1")
    suspend fun getExpenseCategoriesSnapshot(): List<Category>

    @Query("select * from category where type=:type")
    fun getCategoriesOfType(type:Int): LiveData<List<Category>>

}