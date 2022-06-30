package com.lukasz.galinski.fluffy.repository.database

import androidx.room.Dao
import androidx.room.Insert
import com.lukasz.galinski.fluffy.model.DataModel

@Dao
interface TransactionsDao {

    @Insert
    fun createDummyTransaction(dataModel: DataModel)



}