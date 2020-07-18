package az.siftoshka.cubemate.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import az.siftoshka.cubemate.R
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment: Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cToolbar.setExpandedTitleTextAppearance(R.style.CollapsingExpanded)
        cToolbar.setCollapsedTitleTextAppearance(R.style.CollapsingCollapsed)
    }

}