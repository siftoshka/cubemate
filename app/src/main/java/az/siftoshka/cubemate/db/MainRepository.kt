package az.siftoshka.cubemate.db

import javax.inject.Inject

class MainRepository @Inject constructor(private val resultDAO: ResultDAO) {

    suspend fun insertResult(result: Result) = resultDAO.insertResult(result)

    suspend fun deleteResult(result: Result) = resultDAO.deleteResult(result)

    suspend fun deleteAllResults() = resultDAO.deleteAllResults()

    fun getAllResultsByTime() = resultDAO.getAllResultsByTime()

    fun getAllResultsByDate() = resultDAO.getAllResultsByDate()

    fun getAllResultsByType() = resultDAO.getAllResultsByType()

    fun getAvgResult() = resultDAO.getAvgResult()

    fun getBestResult() = resultDAO.getBestResult()

    fun getRecentResult() = resultDAO.getRecentResult()

}