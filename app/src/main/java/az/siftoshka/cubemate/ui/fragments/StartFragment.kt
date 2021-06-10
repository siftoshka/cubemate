package az.siftoshka.cubemate.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import az.siftoshka.cubemate.R
import az.siftoshka.cubemate.databinding.FragmentStartBinding
import az.siftoshka.cubemate.utils.Constants.PREF_LAUNCH
import az.siftoshka.cubemate.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartFragment : Fragment(R.layout.fragment_start) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val binding : FragmentStartBinding by viewBinding(FragmentStartBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firstLaunch()
    }

    private fun firstLaunch() {
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_timerFragment)
            sharedPreferences.edit()
                .putInt(PREF_LAUNCH, 101)
                .apply()
        }
    }
}