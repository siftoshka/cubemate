package az.siftoshka.cubemate.ui.fragments

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_LIGHT
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import az.siftoshka.cubemate.R
import az.siftoshka.cubemate.db.Result
import az.siftoshka.cubemate.ui.viewmodels.MainViewModel
import az.siftoshka.cubemate.utils.Constants.PREF_CUBE
import az.siftoshka.cubemate.utils.Constants.PREF_LAUNCH
import az.siftoshka.cubemate.utils.Constants.PREF_MODE
import az.siftoshka.cubemate.utils.MainListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_timer.*
import kotlinx.android.synthetic.main.fragment_timer.animationImage
import kotlinx.android.synthetic.main.fragment_timer_alt.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class TimerFragment : Fragment(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var lightSensor: Sensor? = null
    private var mainListener: MainListener? = null

    private var tapMode = 0
    private var alreadyReady = false
    private var alreadyStart = false
    private var alreadyFinish = false
    private var type: String? = null
    private var isReady = false
    private var isStarted = false
    private var isActive = false
    private var isEnoughLight = false

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val viewModel: MainViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainListener) mainListener = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tapMode = sharedPreferences.getInt(PREF_MODE, 0)

        return if (tapMode == 101) inflater.inflate(R.layout.fragment_timer, container, false)
        else inflater.inflate(R.layout.fragment_timer_alt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getTapModeFunctionality()
        type = sharedPreferences.getString(PREF_CUBE, null)
        firstLaunch()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == TYPE_LIGHT) getLightSensor(event)
    }

    private fun getLightSensor(event: SensorEvent?) {
        val value = event?.values?.get(0)?.toInt()
        if (value != null && tapMode == 101) {
            if (value > currentMin(value)) isEnoughLight = true
            test?.text = value.toString()
            if (value <= currentMin(value) && isEnoughLight) {
                when (isActive && isReady) {
                    true -> {
                        Timber.d("FINISH")
                        isReady = false
                        isActive = false
                        sensorManager?.unregisterListener(this)
                        finishSensorStage()
                    }
                    false -> {
                        Timber.d("READY")
                        isReady = true
                        readySensorStage()
                    }
                }
            }
            if (value > currentMin(value) && isReady) {
                Timber.d("START")
                isActive = true
                startSensorStage()
            }
        }
    }

    private fun getTapModeFunctionality() {
        if (tapMode != 101) {
            animationImage.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> if (!isStarted) readyStage()
                    MotionEvent.ACTION_UP -> {
                        if (isStarted) finishStage()
                        else {
                            startStage()
                            v.performClick()
                        }
                    }
                }
                true
            }
        }
    }

    private fun showMessage(result: Result) {
        alreadyReady = false
        alreadyStart = false
        alreadyFinish = false
        if (tapMode == 101) {
            mainListener?.showMessage("Your score ${result.timeInSeconds} seconds is saved")
            viewModel.insertResult(result)
            tryAgainButton?.visibility = View.VISIBLE
            animationImage?.visibility = View.GONE
            sensorText.visibility = View.INVISIBLE
            tryAgainButton?.setOnClickListener {
                registerSensor()
                tryAgainButton.visibility = View.GONE
                animationImage?.visibility = View.VISIBLE
                sensorText.visibility = View.VISIBLE
            }
        } else {
            mainListener?.showMessage("Your score ${result.timeInSeconds} seconds is saved")
            viewModel.insertResult(result)
        }
    }

    private fun readyStage() {
        animationImage?.visibility = View.INVISIBLE
        chronometer?.setTextColor(resources.getColor(R.color.red))
        chronometer?.textSize = 30F
        tapText?.text = resources.getString(R.string.ready)
    }

    private fun startStage() {
        animationImage?.visibility = View.VISIBLE
        animationImage?.setAnimation(R.raw.puzzle)
        animationImage?.playAnimation()
        isStarted = true
        chronometer?.base = SystemClock.elapsedRealtime()
        chronometer?.start()
        chronometer?.setTextColor(resources.getColor(R.color.colorPrimary))
        chronometer?.textSize = 50F
        tapText?.text = resources.getString(R.string.go)
    }

    private fun finishStage() {
        animationImage.visibility = View.VISIBLE
        animationImage?.setAnimation(R.raw.pulse)
        animationImage?.playAnimation()
        isStarted = false
        val elapsedMillis = (SystemClock.elapsedRealtime() - chronometer.base)
        chronometer.stop()
        if (type == null) type = "3x3"
        val result = Result(elapsedMillis.toFloat() / 1000, Date().time, type, typePriority(type))
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.setTextColor(resources.getColor(R.color.colorPrimary))
        chronometer.textSize = 50F
        showMessage(result)
    }

    private fun currentMin(value: Int?): Int {
        return when (value) {
            in 30..1000 -> 20
            in 1000..10000 -> 10
            in 10000..50000 -> 1000
            else -> 20
        }
    }

    private fun readySensorStage() {
        if (!alreadyReady) {
            Timber.d("Already ready: $alreadyReady")
            animationImage?.visibility = View.INVISIBLE
            sChronometer?.setTextColor(resources.getColor(R.color.red))
            sChronometer?.textSize = 30F
            sensorText?.text = resources.getString(R.string.ready)
            alreadyReady = true
        }
    }

    private fun startSensorStage() {
        if (!alreadyStart) {
            Timber.d("Already start: $alreadyStart")
            animationImage?.visibility = View.VISIBLE
            animationImage?.setAnimation(R.raw.puzzle)
            animationImage?.playAnimation()
            sChronometer?.base = SystemClock.elapsedRealtime()
            sChronometer?.start()
            sChronometer?.setTextColor(resources.getColor(R.color.colorPrimary))
            sChronometer?.textSize = 50F
            sensorText?.text = resources.getString(R.string.go)
            alreadyStart = true
        }
    }

    private fun finishSensorStage() {
        if (!alreadyFinish) {
            Timber.d("Already finish: $alreadyFinish")
            alreadyFinish = true
            animationImage.visibility = View.VISIBLE
            animationImage?.setAnimation(R.raw.pulse)
            animationImage?.playAnimation()
            val elapsedMillis = (SystemClock.elapsedRealtime() - sChronometer.base)
            sChronometer.stop()
            if (type == null) type = "3x3"
            val result =
                Result(elapsedMillis.toFloat() / 1000, Date().time, type, typePriority(type))
            sChronometer.base = SystemClock.elapsedRealtime()
            sChronometer.setTextColor(resources.getColor(R.color.colorPrimary))
            sChronometer.textSize = 50F
            showMessage(result)
        }
    }

    private fun registerSensor() {
        sensorManager = activity?.getSystemService(SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager!!.getDefaultSensor(TYPE_LIGHT)

        if (lightSensor == null) unsupportedSensor()
        lightSensor.also {
            sensorManager!!.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    private fun typePriority(type: String?): Int {
        return when (type) {
            "3x3" -> 3
            "4x4" -> 4
            "5x5" -> 5
            "6x6" -> 6
            "7x7" -> 7
            else -> 3
        }
    }

    private fun firstLaunch() {
      val launch = sharedPreferences.getInt(PREF_LAUNCH, 0)
        if (launch != 101)
            findNavController().navigate(R.id.action_timerFragment_to_startFragment)
    }

    private fun unsupportedSensor() {
        if (tapMode == 101)
            sensorText?.text = resources.getString(R.string.unsupported)
    }

    override fun onResume() {
        super.onResume()
        registerSensor()
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onAccuracyChanged(event: Sensor?, digit: Int) {}
}
