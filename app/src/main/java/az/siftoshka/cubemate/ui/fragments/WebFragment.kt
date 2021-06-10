package az.siftoshka.cubemate.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import az.siftoshka.cubemate.R
import az.siftoshka.cubemate.databinding.FragmentWebBinding
import az.siftoshka.cubemate.utils.Constants.PREF_WEB
import az.siftoshka.cubemate.utils.Constants.URL_LICENSES
import az.siftoshka.cubemate.utils.Constants.URL_PRIVACY_POLICY
import az.siftoshka.cubemate.utils.Constants.URL_TERMS_OF_PRIVACY
import az.siftoshka.cubemate.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WebFragment : Fragment(R.layout.fragment_web) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val binding : FragmentWebBinding by viewBinding(FragmentWebBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showWeb(sharedPreferences.getInt(PREF_WEB, 0))
    }

    private fun showWeb(index: Int) {
        when (index) {
            101 -> binding.web.loadUrl(URL_PRIVACY_POLICY)
            102 -> binding.web.loadUrl(URL_TERMS_OF_PRIVACY)
            103 -> binding.web.loadUrl(URL_LICENSES)
        }
    }
}