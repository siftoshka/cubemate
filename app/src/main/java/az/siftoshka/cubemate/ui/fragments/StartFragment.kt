package az.siftoshka.cubemate.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import az.siftoshka.cubemate.R
import kotlinx.android.synthetic.main.fragment_start.*

class StartFragment : Fragment(R.layout.fragment_start) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button.setOnClickListener {
        }
    }

}