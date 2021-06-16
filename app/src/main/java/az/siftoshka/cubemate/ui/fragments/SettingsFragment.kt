package az.siftoshka.cubemate.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import az.siftoshka.cubemate.R
import az.siftoshka.cubemate.databinding.FragmentSettingsBinding
import az.siftoshka.cubemate.utils.*
import az.siftoshka.cubemate.utils.Constants.PREF_CUBE
import az.siftoshka.cubemate.utils.Constants.PREF_MODE
import az.siftoshka.cubemate.utils.Constants.PREF_SORT
import az.siftoshka.cubemate.utils.Constants.PREF_SORT_POS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val binding : FragmentSettingsBinding by viewBinding(FragmentSettingsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.cToolbar.setExpandedTitleTextAppearance(R.style.CollapsingExpanded)
        binding.cToolbar.setCollapsedTitleTextAppearance(R.style.CollapsingCollapsed)
        binding.telegramContact.setOnClickListener { requireContext().openTelegram() }
        binding.githubContact.setOnClickListener { requireContext().openGithub() }
        binding.instagramContact.setOnClickListener { requireContext().openInstagram() }
        binding.rateButton.setOnClickListener { requireContext().openGooglePlay() }
        binding.privacyPolicy.setOnClickListener { showPrivacyPolicyScreen() }
        binding.termsOfService.setOnClickListener { showTermsOfServiceScreen() }
        binding.license.setOnClickListener { showLicenses() }
        requireContext().textDesignerOktay(binding.creditsOktay)
        requireContext().textDesignerFreepik(binding.creditsFreepik)
        modeSwitcher()
        spinner()
        spinnerSort()
    }

    private fun modeSwitcher() {
        val tapMode = sharedPreferences.getInt(PREF_MODE, 0)

        binding.modeSwitcher.isChecked = tapMode == 101
        binding.modeSwitcher.setOnCheckedChangeListener { _, b ->
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

    private fun spinner() {
        val types = resources.getStringArray(R.array.Types)
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_txt, types)
        val spinnerItem = sharedPreferences.getString(PREF_CUBE, null)
        GlobalScope.launch {
            binding.spinner.setSelection(types.indexOf(spinnerItem))
        }
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
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
            binding.spinnerSort.setSelection(types.indexOf(spinnerItem))
        }
        binding.spinnerSort.adapter = adapter
        binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                sharedPreferences.edit()
                    .putString(PREF_SORT, types[position])
                    .putInt(PREF_SORT_POS, position)
                    .apply()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun showPrivacyPolicyScreen() {
        findNavController().navigate(R.id.action_settingsFragment_to_webFragment, bundleOf(Pair(Constants.EXTRA_WEB, 101)))
    }

    private fun showTermsOfServiceScreen() {
        findNavController().navigate(R.id.action_settingsFragment_to_webFragment, bundleOf(Pair(Constants.EXTRA_WEB, 102)))
    }

    private fun showLicenses() {
        findNavController().navigate(R.id.action_settingsFragment_to_webFragment, bundleOf(Pair(Constants.EXTRA_WEB, 103)))
    }

}