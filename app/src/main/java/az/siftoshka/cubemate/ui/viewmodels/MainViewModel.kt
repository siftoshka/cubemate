package az.siftoshka.cubemate.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.siftoshka.cubemate.db.MainRepository
import az.siftoshka.cubemate.db.Result
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(private val mainRepository: MainRepository): ViewModel() {

    val resultsByTime = mainRepository.getAllResultsByTime()
    private val resultsByDate = mainRepository.getAllResultsByDate()
    private val resultsByType = mainRepository.getAllResultsByType()
    val avgResult = mainRepository.getAvgResult()

    fun insertResult(result: Result) = viewModelScope.launch {
        mainRepository.insertResult(result)
    }

    fun deleteResult(result: Result) = viewModelScope.launch {
        mainRepository.deleteResult(result)
    }
}