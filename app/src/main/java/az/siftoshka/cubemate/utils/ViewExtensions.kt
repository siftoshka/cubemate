package az.siftoshka.cubemate.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import az.siftoshka.cubemate.R

fun View.color(@ColorRes colorResId: Int) = ContextCompat.getColor(this.context, colorResId)

fun Context.openTelegram() {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(Constants.DEV_TELEGRAM)
    startActivity(intent)
}

fun Context.openGithub() {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(Constants.DEV_GITHUB)
    startActivity(intent)
}

fun Context.openInstagram() {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(Constants.DEV_INSTAGRAM)
    startActivity(intent)
}

fun Context.openGooglePlay() {
    try {
        startActivity(
            Intent(Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.google_play_market, this.packageName))))
    } catch (anfe: ActivityNotFoundException) {
        startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.google_play_store, this.packageName))))
    }
}

fun Context.textDesignerOktay(textView: TextView) {
    val text = textView.text.toString()
    val spannableStringOktay = SpannableString(text)
    val clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(textView: View) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(Constants.DESIGNER_OKTAY)
            startActivity(intent)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }
    spannableStringOktay.setSpan(clickableSpan, 25, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    textView.text = spannableStringOktay
    textView.movementMethod = LinkMovementMethod.getInstance()
}

fun Context.textDesignerFreepik(textView: TextView) {
    val text = textView.text.toString()
    val spannableStringFreepik = SpannableString(text)
    val clickableSpan1: ClickableSpan = object : ClickableSpan() {
        override fun onClick(textView: View) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(Constants.DESIGNER_FREEPIK)
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
            intent.data = Uri.parse(Constants.FLATICON)
            startActivity(intent)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }
    spannableStringFreepik.setSpan(clickableSpan1, 14, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    spannableStringFreepik.setSpan(clickableSpan2, 27, 43, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    textView.text = spannableStringFreepik
    textView.movementMethod = LinkMovementMethod.getInstance()
}