package az.siftoshka.cubemate.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import az.siftoshka.cubemate.R
import az.siftoshka.cubemate.utils.Constants.PREF_WEB
import az.siftoshka.cubemate.utils.Constants.URL_LICENSES
import az.siftoshka.cubemate.utils.Constants.URL_PRIVACY_POLICY
import az.siftoshka.cubemate.utils.Constants.URL_TERMS_OF_PRIVACY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_web.*
import javax.inject.Inject

@AndroidEntryPoint
class WebFragment : Fragment(R.layout.fragment_web) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showWeb(sharedPreferences.getInt(PREF_WEB, 0))
    }

    private fun showWeb(index: Int) {
        when (index) {
            101 -> web?.loadUrl(URL_PRIVACY_POLICY)
            102 -> web?.loadUrl(URL_TERMS_OF_PRIVACY)
            103 -> web?.loadUrl(URL_LICENSES)
        }
    }


}