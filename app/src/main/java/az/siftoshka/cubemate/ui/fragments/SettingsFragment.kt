package az.siftoshka.cubemate.ui.fragments

import android.content.ActivityNotFoundException
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.fragment.app.Fragment
import az.siftoshka.cubemate.Constants.DESIGNER_FREEPIK
import az.siftoshka.cubemate.Constants.DESIGNER_OKTAY
import az.siftoshka.cubemate.Constants.DEV_GITHUB
import az.siftoshka.cubemate.Constants.DEV_INSTAGRAM
import az.siftoshka.cubemate.Constants.DEV_TELEGRAM
import az.siftoshka.cubemate.Constants.FLATICON
import az.siftoshka.cubemate.R
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cToolbar.setExpandedTitleTextAppearance(R.style.CollapsingExpanded)
        cToolbar.setCollapsedTitleTextAppearance(R.style.CollapsingCollapsed)
        telegramContact.setOnClickListener { showTelegramPage() }
        githubContact.setOnClickListener { showGithubPage() }
        instagramContact.setOnClickListener { showInstagramPage() }
        rateButton.setOnClickListener { showGooglePlay() }
        spannableCreditOktay()
        spannableCreditFreepik()
        modeSwitcher()
    }

    private fun modeSwitcher() {
        val prefs = requireContext().getSharedPreferences("Tap-Mode", MODE_PRIVATE)
        val tapMode = prefs.getInt("Tap", 0)

        modeSwitcher.isChecked = tapMode == 101
        modeSwitcher.setOnCheckedChangeListener { _, b ->
            if (b) {
                val editor = requireContext().getSharedPreferences(
                    "Tap-Mode",
                    MODE_PRIVATE
                ).edit()
                editor.putInt("Tap", 101)
                editor.apply()
            } else {
                val editor = requireContext().getSharedPreferences(
                    "Tap-Mode",
                    MODE_PRIVATE
                ).edit()
                editor.putInt("Tap", 100)
                editor.apply()
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

}