package az.siftoshka.cubemate.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import az.siftoshka.cubemate.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            window.navigationBarColor = resources.getColor(R.color.mainBackground)


        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())


        navHostFragment.findNavController()
            .addOnDestinationChangedListener { controller, destination, arguments ->
                val prefs = getSharedPreferences("Tap-Mode", Context.MODE_PRIVATE)
                val tapMode = prefs.getInt("Tap", 0)
                when (destination.id) {
                    R.id.settingsFragment, R.id.timerFragment, R.id.statisticsFragment -> {
                        Timber.d("NAVBAR")
                        if (destination.id == R.id.timerFragment && tapMode == 100) hideStatusBar()
                        else showStatusBar()
                        bottomNavigationView.visibility = View.VISIBLE
                    }
                    else -> bottomNavigationView.visibility = View.GONE
                }
            }
    }

    private fun showStatusBar() {
        window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private fun hideStatusBar() {
        window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}