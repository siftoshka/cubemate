package az.siftoshka.cubemate.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import az.siftoshka.cubemate.R
import az.siftoshka.cubemate.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimerFragment: Fragment(R.layout.fragment_timer) {

    private val viewModel: MainViewModel by viewModels()

}