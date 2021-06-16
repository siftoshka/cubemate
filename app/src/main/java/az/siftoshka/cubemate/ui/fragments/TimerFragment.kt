package az.siftoshka.cubemate.ui.fragments

import android.annotation.SuppressLint
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
import az.siftoshka.cubemate.databinding.FragmentTimerAltBinding
import az.siftoshka.cubemate.databinding.FragmentTimerBinding
import az.siftoshka.cubemate.db.Result
import az.siftoshka.cubemate.ui.viewmodels.MainViewModel
import az.siftoshka.cubemate.utils.*
import az.siftoshka.cubemate.utils.Constants.PREF_CUBE
import az.siftoshka.cubemate.utils.Constants.PREF_LAUNCH
import az.siftoshka.cubemate.utils.Constants.PREF_MODE
import dagger.hilt.android.AndroidEntryPoint
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

    private val binding : FragmentTimerBinding by viewBinding(FragmentTimerBinding::bind)
    private val bindingAlt : FragmentTimerAltBinding by viewBinding(FragmentTimerAltBinding::bind)

    private val viewModel: MainViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainListener) mainListener = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        tapMode = sharedPreferences.getInt(PREF_MODE, 0)

        return if (tapMode == 101) { inflater.inflate(R.layout.fragment_timer, container, false) }
        else { inflater.inflate(R.layout.fragment_timer_alt, container, false) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getTapModeFunctionality()
        showRecentResult()
        showBestResult()
        type = sharedPreferences.getString(PREF_CUBE, null)
        firstLaunch()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == TYPE_LIGHT) { getLightSensor(event) }
    }

    private fun getLightSensor(event: SensorEvent?) {
        val value = event?.values?.get(0)?.toInt()
        if (value != null && tapMode == 101) {
            if (value > currentMin(value)) isEnoughLight = true
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

    @SuppressLint("ClickableViewAccessibility")
    private fun getTapModeFunctionality() {
        if (tapMode != 101) {
            bindingAlt.animationImage.setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> if (!isStarted) readyStage()
                    MotionEvent.ACTION_UP -> {
                        if (isStarted) { finishStage() }
                        else {
                            startStage()
                            view.performClick()
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
            mainListener?.showMessage(getString(R.string.score_text_result, result.timeInSeconds.round(2)))
            viewModel.insertResult(result)
            binding.tryAgainButton.visibility = View.VISIBLE
            binding.animationImage.visibility = View.GONE
            binding.sensorText.visibility = View.INVISIBLE
            binding.tryAgainButton.setOnClickListener {
                registerSensor()
                binding.tryAgainButton.visibility = View.GONE
                binding.animationImage.visibility = View.VISIBLE
                binding.sensorText.visibility = View.VISIBLE
            }
        } else {
            mainListener?.showMessage(getString(R.string.score_text_result, result.timeInSeconds.round(2)))
            viewModel.insertResult(result)
        }
    }

    private fun readyStage() {
        bindingAlt.animationImage.visibility = View.INVISIBLE
        bindingAlt.chronometer.setTextColor(requireView().color(R.color.red))
        bindingAlt.chronometer.textSize = 30F
        bindingAlt.tapText.text = resources.getString(R.string.ready)
    }

    private fun startStage() {
        bindingAlt.animationImage.visibility = View.VISIBLE
        bindingAlt.animationImage.setAnimation(R.raw.puzzle)
        bindingAlt.animationImage.playAnimation()
        isStarted = true
        bindingAlt.chronometer.base = SystemClock.elapsedRealtime()
        bindingAlt.chronometer.start()
        bindingAlt.chronometer.setTextColor(requireView().color(R.color.colorPrimary))
        bindingAlt.chronometer.textSize = 50F
        bindingAlt.tapText.text = resources.getString(R.string.go)
    }

    private fun finishStage() {
        bindingAlt.animationImage.visibility = View.VISIBLE
        bindingAlt.animationImage.setAnimation(R.raw.pulse)
        bindingAlt.animationImage.playAnimation()
        isStarted = false
        val elapsedMillis = (SystemClock.elapsedRealtime() - bindingAlt.chronometer.base)
        bindingAlt.chronometer.stop()
        if (type == null) type = "3x3"
        val result = Result(elapsedMillis.toFloat() / 1000, Date().time, type, typePriority(type))
        bindingAlt.chronometer.base = SystemClock.elapsedRealtime()
        bindingAlt.chronometer.setTextColor(resources.getColor(R.color.colorPrimary))
        bindingAlt.chronometer.textSize = 50F
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
            binding.animationImage.visibility = View.INVISIBLE
            binding.sChronometer.setTextColor(requireView().color(R.color.red))
            binding.sChronometer.textSize = 30F
            binding.sensorText.text = resources.getString(R.string.ready)
            alreadyReady = true
        }
    }

    private fun startSensorStage() {
        if (!alreadyStart) {
            Timber.d("Already start: $alreadyStart")
            binding.animationImage.visibility = View.VISIBLE
            binding.animationImage.setAnimation(R.raw.puzzle)
            binding.animationImage.playAnimation()
            binding.sChronometer.base = SystemClock.elapsedRealtime()
            binding.sChronometer.start()
            binding.sChronometer.setTextColor(requireView().color(R.color.colorPrimary))
            binding.sChronometer.textSize = 50F
            binding.sensorText.text = resources.getString(R.string.go)
            alreadyStart = true
        }
    }

    private fun finishSensorStage() {
        if (!alreadyFinish) {
            Timber.d("Already finish: $alreadyFinish")
            alreadyFinish = true
            binding.animationImage.visibility = View.VISIBLE
            binding.animationImage.setAnimation(R.raw.pulse)
            binding.animationImage.playAnimation()
            val elapsedMillis = (SystemClock.elapsedRealtime() - binding.sChronometer.base)
            binding.sChronometer.stop()
            if (type == null) { type = "3x3" }
            val result = Result(elapsedMillis.toFloat() / 1000, Date().time, type, typePriority(type))
            binding.sChronometer.base = SystemClock.elapsedRealtime()
            binding.sChronometer.setTextColor(resources.getColor(R.color.colorPrimary))
            binding.sChronometer.textSize = 32F
            showMessage(result)
        }
    }

    private fun registerSensor() {
        sensorManager = activity?.getSystemService(SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager!!.getDefaultSensor(TYPE_LIGHT)

        if (lightSensor == null) { unsupportedSensor() }
        lightSensor.let { sensorManager!!.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST) }
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

    private fun showRecentResult() {
        if (tapMode != 101) {
            viewModel.recentResult.observe(viewLifecycleOwner) {
                if (it != null) {
                    bindingAlt.recentResultText.visibility = View.VISIBLE
                    bindingAlt.recentResultLayout.visibility = View.VISIBLE
                    bindingAlt.scoreTextRecent.text = it.timeInSeconds.toString()
                    bindingAlt.typeTextRecent.text = it.type
                    bindingAlt.dateTextRecent.text = Converter.timeToDate(it.timestamp)
                }
            }
        } else {
            viewModel.recentResult.observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.sRecentResultText.visibility = View.VISIBLE
                    binding.sRecentResultLayout.visibility = View.VISIBLE
                    binding.sScoreTextRecent.text = it.timeInSeconds.toString()
                    binding.sTypeTextRecent.text = it.type
                    binding.sDateTextRecent.text = Converter.timeToDate(it.timestamp)
                }
            }
        }
    }

    private fun showBestResult() {
        if (tapMode != 101) {
            viewModel.bestResult.observe(viewLifecycleOwner) {
                if (it != null) {
                    bindingAlt.bestResultText.visibility = View.VISIBLE
                    bindingAlt.bestResultLayout.visibility = View.VISIBLE
                    bindingAlt.scoreTextBest.text = it.timeInSeconds.toString()
                    bindingAlt.typeTextBest.text = it.type
                    bindingAlt.dateTextBest.text = Converter.timeToDate(it.timestamp)
                }
            }
        } else {
            viewModel.bestResult.observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.sBestResultText.visibility = View.VISIBLE
                    binding.sBestResultLayout.visibility = View.VISIBLE
                    binding.sScoreTextBest.text = it.timeInSeconds.toString()
                    binding.sTypeTextBest.text = it.type
                    binding.sDateTextBest.text = Converter.timeToDate(it.timestamp)
                }
            }
        }
    }

    private fun firstLaunch() {
      val launch = sharedPreferences.getInt(PREF_LAUNCH, 0)
        if (launch != 101) { findNavController().navigate(R.id.action_timerFragment_to_startFragment) }
    }

    private fun unsupportedSensor() {
        if (tapMode == 101) { binding.sensorText.text = resources.getString(R.string.unsupported) }
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
