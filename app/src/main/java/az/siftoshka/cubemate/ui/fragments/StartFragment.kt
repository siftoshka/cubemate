package az.siftoshka.cubemate.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import az.siftoshka.cubemate.R
import az.siftoshka.cubemate.utils.Constants.PREF_LAUNCH
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_start.*
import javax.inject.Inject

@AndroidEntryPoint
class StartFragment : Fragment(R.layout.fragment_start) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firstLaunch()
    }

    private fun firstLaunch() {
        val launch = sharedPreferences.getInt(PREF_LAUNCH, 0)
        if (launch != 101) {
            button.setOnClickListener {
                findNavController().navigate(R.id.action_startFragment_to_timerFragment)
                sharedPreferences.edit()
                    .putInt(PREF_LAUNCH, 101)
                    .apply()
            }
        } else
            findNavController().navigate(R.id.action_startFragment_to_timerFragment)
    }
}