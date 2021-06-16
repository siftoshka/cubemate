package az.siftoshka.cubemate.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import az.siftoshka.cubemate.R
import az.siftoshka.cubemate.databinding.FragmentWebBinding
import az.siftoshka.cubemate.utils.Constants.EXTRA_WEB
import az.siftoshka.cubemate.utils.Constants.URL_LICENSES
import az.siftoshka.cubemate.utils.Constants.URL_PRIVACY_POLICY
import az.siftoshka.cubemate.utils.Constants.URL_TERMS_OF_PRIVACY
import az.siftoshka.cubemate.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebFragment : Fragment(R.layout.fragment_web) {

    private val binding : FragmentWebBinding by viewBinding(FragmentWebBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        when(arguments?.get(EXTRA_WEB) ?: 0) {
            101 -> binding.web.loadUrl(URL_PRIVACY_POLICY)
            102 -> binding.web.loadUrl(URL_TERMS_OF_PRIVACY)
            103 -> binding.web.loadUrl(URL_LICENSES)
        }
    }
}