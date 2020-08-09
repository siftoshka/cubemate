package az.siftoshka.cubemate.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import az.siftoshka.cubemate.db.MainRepository
import az.siftoshka.cubemate.db.Result
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel @ViewModelInject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    val resultsByTime = mainRepository.getAllResultsByTime().asLiveData(viewModelScope.coroutineContext)
    val resultsByDate = mainRepository.getAllResultsByDate().asLiveData(viewModelScope.coroutineContext)
    val resultsByType = mainRepository.getAllResultsByType().asLiveData(viewModelScope.coroutineContext)
    val avgResult = mainRepository.getAvgResult().asLiveData(viewModelScope.coroutineContext)
    val bestResult = mainRepository.getBestResult().asLiveData(viewModelScope.coroutineContext)
    val recentResult = mainRepository.getRecentResult().asLiveData(viewModelScope.coroutineContext)

    fun insertResult(result: Result) = viewModelScope.launch {
        mainRepository.insertResult(result)
    }

    fun deleteResult(result: Result) = viewModelScope.launch {
        mainRepository.deleteResult(result)
    }
}