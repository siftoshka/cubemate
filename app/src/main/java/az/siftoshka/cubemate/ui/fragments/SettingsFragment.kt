package az.siftoshka.cubemate.ui.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import az.siftoshka.cubemate.R
import az.siftoshka.cubemate.ui.viewmodels.MainViewModel
import az.siftoshka.cubemate.utils.Constants
import az.siftoshka.cubemate.utils.Constants.DESIGNER_FREEPIK
import az.siftoshka.cubemate.utils.Constants.DESIGNER_OKTAY
import az.siftoshka.cubemate.utils.Constants.DEV_GITHUB
import az.siftoshka.cubemate.utils.Constants.DEV_INSTAGRAM
import az.siftoshka.cubemate.utils.Constants.DEV_TELEGRAM
import az.siftoshka.cubemate.utils.Constants.FLATICON
import az.siftoshka.cubemate.utils.Constants.PREF_CUBE
import az.siftoshka.cubemate.utils.Constants.PREF_MODE
import az.siftoshka.cubemate.utils.Constants.PREF_SORT
import az.siftoshka.cubemate.utils.Constants.PREF_SORT_POS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cToolbar.setExpandedTitleTextAppearance(R.style.CollapsingExpanded)
        cToolbar.setCollapsedTitleTextAppearance(R.style.CollapsingCollapsed)
        telegramContact.setOnClickListener { showTelegramPage() }
        githubContact.setOnClickListener { showGithubPage() }
        instagramContact.setOnClickListener { showInstagramPage() }
        rateButton.setOnClickListener { showGooglePlay() }
        privacyPolicy.setOnClickListener { showPrivacyPolicyScreen() }
        termsOfService.setOnClickListener { showTermsOfServiceScreen() }
        license.setOnClickListener { showLicenses() }
        spannableCreditOktay()
        spannableCreditFreepik()
        modeSwitcher()
        spinner()
        spinnerSort()
    }

    private fun modeSwitcher() {
        val tapMode = sharedPreferences.getInt(PREF_MODE, 0)

        modeSwitcher.isChecked = tapMode == 101
        modeSwitcher.setOnCheckedChangeListener { _, b ->
            if (b) {
                sharedPreferences.edit()
                    .putInt(PREF_MODE, 101)
                    .apply()
            } else {
                sharedPreferences.edit()
                    .putInt(PREF_MODE, 100)
                    .apply()
            }
        }
    }

    private fun showTelegramPage() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(DEV_TELEGRAM)
        startActivity(intent)
    }

    private fun showGithubPage() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(DEV_GITHUB)
        startActivity(intent)
    }

    private fun showInstagramPage() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(DEV_INSTAGRAM)
        startActivity(intent)
    }

    private fun showGooglePlay() {
        val appPackageName = requireContext().packageName
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (anfe: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }

    private fun spannableCreditOktay() {
        val cOktay = creditsOktay.text.toString()
        val spannableStringOktay = SpannableString(cOktay)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(DESIGNER_OKTAY)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        spannableStringOktay.setSpan(clickableSpan, 25, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        creditsOktay.text = spannableStringOktay
        creditsOktay.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun spannableCreditFreepik() {
        val cFreepik = creditsFreepik.text.toString()
        val spannableStringFreepik = SpannableString(cFreepik)
        val clickableSpan1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(DESIGNER_FREEPIK)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        val clickableSpan2: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(FLATICON)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        spannableStringFreepik.setSpan(clickableSpan1, 14, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableStringFreepik.setSpan(clickableSpan2, 27, 43, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        creditsFreepik.text = spannableStringFreepik
        creditsFreepik.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun spinner() {
        val types = resources.getStringArray(R.array.Types)
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_txt, types)
        val spinnerItem = sharedPreferences.getString(PREF_CUBE, null)
        GlobalScope.launch {
            spinner?.setSelection(types.indexOf(spinnerItem))
        }
        spinner?.adapter = adapter
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                sharedPreferences.edit()
                    .putString(PREF_CUBE, types[position])
                    .apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun spinnerSort() {
        val types = resources.getStringArray(R.array.Sort)
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_txt, types)
        val spinnerItem = sharedPreferences.getString(PREF_SORT, null)
        GlobalScope.launch {
            Timber.d("$spinnerItem")
            spinnerSort?.setSelection(types.indexOf(spinnerItem))
        }
        spinnerSort?.adapter = adapter
        spinnerSort?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                sharedPreferences.edit()
                    .putString(PREF_SORT, types[position])
                    .putInt(PREF_SORT_POS, position)
                    .apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun showPrivacyPolicyScreen() {
        sharedPreferences.edit()
            .putInt(Constants.PREF_WEB, 101)
            .apply().also {
                findNavController().navigate(R.id.action_settingsFragment_to_webFragment)
            }
    }

    private fun showTermsOfServiceScreen() {
        sharedPreferences.edit()
            .putInt(Constants.PREF_WEB, 102)
            .apply().also {
                findNavController().navigate(R.id.action_settingsFragment_to_webFragment)
            }
    }

    private fun showLicenses() {
        sharedPreferences.edit()
            .putInt(Constants.PREF_WEB, 103)
            .apply().also {
                findNavController().navigate(R.id.action_settingsFragment_to_webFragment)
            }
    }

}