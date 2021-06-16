package az.siftoshka.cubemate.ui

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import az.siftoshka.cubemate.R
import az.siftoshka.cubemate.utils.Constants.PREF_LAUNCH
import az.siftoshka.cubemate.utils.Constants.PREF_MODE
import az.siftoshka.cubemate.utils.MainListener
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main), MainListener {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firstOpen()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { window.navigationBarColor = resources.getColor(R.color.mainBackground, null) }


        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())
        val navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
                val tapMode = sharedPreferences.getInt(PREF_MODE, 0)
                when (destination.id) {
                    R.id.settingsFragment, R.id.timerFragment, R.id.statisticsFragment -> {
                        if (destination.id == R.id.timerFragment && tapMode == 101) { hideStatusBar() }
                        else { showStatusBar() }
                        bottomNavigationView.visibility = View.VISIBLE
                    }
                    else -> bottomNavigationView.visibility = View.GONE
                }
            }
    }

    @Suppress("DEPRECATION")
    private fun showStatusBar() = window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

    @Suppress("DEPRECATION")
    private fun hideStatusBar() = window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

    private fun firstOpen() {
        val launch = sharedPreferences.getInt(PREF_LAUNCH, 0)
        if (launch != 101) {
            sharedPreferences.edit()
                .putInt(PREF_LAUNCH, 100)
                .apply()
        }
    }

    override fun showMessage(message: String) {
        val snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
        snackbar.anchorView = bottomNavigationView
        snackbar.show()
    }
}