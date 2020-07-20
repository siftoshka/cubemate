package az.siftoshka.cubemate.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import az.siftoshka.cubemate.R
import kotlinx.android.synthetic.main.fragment_start.*

class StartFragment : Fragment(R.layout.fragment_start) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_timerFragment)
            val editor = requireContext().getSharedPreferences(
                "First-Open",
                Context.MODE_PRIVATE
            ).edit()
            editor.putInt("Launch", 101)
            editor.apply()
        }
    }

}