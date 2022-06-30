package com.lukasz.galinski.fluffy.repository.remote

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lukasz.galinski.fluffy.model.DataModel
import com.lukasz.galinski.fluffy.repository.database.TransactionsDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

private const val TRANSACTIONS_LOG = "TRANSACTIONS"
private const val DUMMY_JSON = "DummyTransactions.json"

class NetworkRepository @Inject constructor(
    private val context: Context,
    private val transactionsDao: TransactionsDao
) {

    private fun getJsonDataFromAsset(context: Context): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(DUMMY_JSON).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    fun getTransactions(userId: Long): Flow<ArrayList<DataModel>> {
        return flow {
            val jsonFileString = getJsonDataFromAsset(context)
            Log.i(TRANSACTIONS_LOG, "Loading data")
            val gson = Gson()
            val listPersonType = object : TypeToken<ArrayList<DataModel>>() {}.type
            val persons: ArrayList<DataModel> = gson.fromJson(jsonFileString, listPersonType)
            persons.forEachIndexed { idx, person ->
                Log.i(
                    "NetworkRepository",
                    "> Item $idx:\n$person"
                )
            }

            // val persons = transactionsDao
            emit(persons)
        }
    }

    fun addTransaction(dataModel: DataModel){
        transactionsDao.createDummyTransaction(dataModel)
    }
}