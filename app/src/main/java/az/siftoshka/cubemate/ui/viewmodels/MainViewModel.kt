package az.siftoshka.cubemate.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.siftoshka.cubemate.db.MainRepository
import az.siftoshka.cubemate.db.Result
import kotlinx.coroutines.launch
import timber.log.Timber


class MainViewModel @ViewModelInject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    val resultsByTime = mainRepository.getAllResultsByTime()
    val resultsByDate = mainRepository.getAllResultsByDate()
    val resultsByType = mainRepository.getAllResultsByType()
    val avgResult = mainRepository.getAvgResult()
    val bestResult = mainRepository.getBestResult()
    val recentResult = mainRepository.getRecentResult()

    fun insertResult(result: Result) = viewModelScope.launch {
        mainRepository.insertResult(result)
    }

    fun deleteResult(result: Result) = viewModelScope.launch {
        mainRepository.deleteResult(result)
    }
}