package az.siftoshka.cubemate.ui.fragments

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_LIGHT
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import az.siftoshka.cubemate.R
import az.siftoshka.cubemate.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_timer.*
import timber.log.Timber


@AndroidEntryPoint
class TimerFragment : Fragment(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var lightSensor: Sensor? = null

    private var tapMode = 0
    private var isReady = false
    private var isStarted = false
    private var isActive = false

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = activity?.getSystemService(SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager!!.getDefaultSensor(TYPE_LIGHT)

        if (lightSensor == null) Timber.e("lightSensor is not working")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val prefs = requireContext().getSharedPreferences("Tap-Mode", Context.MODE_PRIVATE)
        tapMode = prefs.getInt("Tap", 0)

        return if (tapMode == 100) inflater.inflate(R.layout.fragment_timer, container, false)
        else inflater.inflate(R.layout.fragment_timer_alt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getTapModeFunctionality()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == TYPE_LIGHT) getLightSensor(event)
    }

    private fun getLightSensor(event: SensorEvent?) {
        val value = event?.values?.get(0)?.toInt()
        if (value != null) {
            test?.text = value.toString()
            if (value < 15) {
                when (isActive && isReady) {
                    true -> {
                        Timber.d("OK")
                        isActive = false
                        isReady = false
                    }
                    false -> isReady = true
                }
            }
            if (value > 15 && isReady) {
                Timber.d("starting...")
                isActive = true
                isReady = false
            }
        }
    }

    private fun getTapModeFunctionality() {
        if (tapMode == 101) {
            animationImage.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> animationImage.visibility = View.INVISIBLE
                    MotionEvent.ACTION_UP -> {
                        if (isStarted) {
                            animationImage.visibility = View.VISIBLE
                            animationImage?.setAnimation(R.raw.pulse)
                            animationImage?.playAnimation()
                            isStarted = false
                        } else {
                            animationImage.visibility = View.VISIBLE
                            animationImage?.setAnimation(R.raw.puzzle)
                            animationImage?.playAnimation()
                            isStarted = true
                            v.performClick()
                        }
                    }
                }
                true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lightSensor.also {
            sensorManager!!.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onAccuracyChanged(event: Sensor?, digit: Int) { }
}
