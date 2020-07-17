package az.siftoshka.cubemate.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import az.siftoshka.cubemate.db.MainRepository

class MainViewModel @ViewModelInject constructor(val mainRepository: MainRepository): ViewModel() {
}