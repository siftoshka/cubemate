package az.siftoshka.cubemate.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import az.siftoshka.cubemate.R
import az.siftoshka.cubemate.adapters.StatAdapter
import az.siftoshka.cubemate.databinding.FragmentStatisticsBinding
import az.siftoshka.cubemate.db.Result
import az.siftoshka.cubemate.ui.viewmodels.MainViewModel
import az.siftoshka.cubemate.utils.Constants.PREF_SORT_POS
import az.siftoshka.cubemate.utils.Converter
import az.siftoshka.cubemate.utils.color
import az.siftoshka.cubemate.utils.viewBinding
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val binding : FragmentStatisticsBinding by viewBinding(FragmentStatisticsBinding::bind)

    private var statAdapter: StatAdapter ?= null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.cToolbar.setExpandedTitleTextAppearance(R.style.CollapsingExpanded)
        binding.cToolbar.setCollapsedTitleTextAppearance(R.style.CollapsingCollapsed)
        setupRecyclerView()
        observeAverageResult()
        when (sharedPreferences.getInt(PREF_SORT_POS, 0)) {
            0 -> sortByDate()
            1 -> sortByTime()
            2 -> sortByType()
            else -> sortByDate()
        }
    }

    private fun observeAverageResult() = viewModel.avgResult.observe(viewLifecycleOwner) {
        if (it == null) {
            binding.avgScore.textSize = 14F
            binding.avgScore.setTextColor(requireView().color(R.color.gray))
            binding.avgScore.text = getString(R.string.avg_error)
        } else {
            binding.avgScore.text = Converter.roundOffDecimal(it).toString()
        }
    }

    private fun setupRecyclerView() = binding.recyclerView.apply {
        statAdapter = StatAdapter(object : StatAdapter.StatItemClickListener {
            override fun onPostClicked(result: Result) { openDialog(result) }
        })
        adapter = statAdapter
    }

    private fun openDialog(result: Result) {
        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.AppCompatAlertDialogStyle)
        builder.setTitle(getString(R.string.dialog_result_title))
        builder.setMessage(getString(R.string.dialog_result_description))
        builder.setPositiveButton(R.string.positive_dialog_action) { _, _ -> viewModel.deleteResult(result) }
        builder.setNegativeButton(R.string.negative_dialog_action) { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun sortByDate() {
        viewModel.resultsByDate.observe(viewLifecycleOwner) {
            if (it.isEmpty()) { binding.emptyLayout.visibility = View.VISIBLE }
            statAdapter?.statisticList(it)
            setBarChart(it)
        }
    }

    private fun sortByTime() {
        viewModel.resultsByTime.observe(viewLifecycleOwner) {
            if (it.isEmpty()) { binding.emptyLayout.visibility = View.VISIBLE }
            statAdapter?.statisticList(it)
            setBarChart(it)
        }
    }

    private fun sortByType() {
        viewModel.resultsByType.observe(viewLifecycleOwner) {
            if (it.isEmpty()) { binding.emptyLayout.visibility = View.VISIBLE }
            statAdapter?.statisticList(it)
            setBarChart(it)
        }
    }

    private fun setBarChart(results: List<Result>) {
        val entries = ArrayList<BarEntry>()
        results.asReversed().forEachIndexed { index, result -> entries.add(BarEntry(index.toFloat(), result.timeInSeconds)) }
        if(entries.isEmpty() || entries.size < 5) {
            binding.dataEmptyText.visibility = View.VISIBLE
            binding.mainChart.visibility = View.GONE
        } else {
            binding.dataEmptyText.visibility = View.GONE
            binding.mainChart.visibility = View.VISIBLE
        }
        val barDataSet = BarDataSet(entries.takeLast(30), "")
        barDataSet.setDrawValues(false)
        barDataSet.color = requireView().color(R.color.colorPrimary)
        binding.mainChart.data = BarData(barDataSet)
        binding.mainChart.animateY(1000)
        binding.mainChart.legend.isEnabled = false
        binding.mainChart.axisRight.apply {
            setDrawGridLines(false)
            setDrawLabels(false)
            setDrawAxisLine(false)
        }
        binding.mainChart.axisLeft.apply {
            setDrawGridLines(false)
            setDrawLabels(false)
            setDrawAxisLine(false)
        }
        binding.mainChart.xAxis.apply {
            setDrawGridLines(false)
            setDrawLabels(false)
            setDrawAxisLine(false)
        }
        binding.mainChart.data.isHighlightEnabled = false
        binding.mainChart.setDrawGridBackground(false)
        binding.mainChart.setDrawBorders(false)
        binding.mainChart.description.isEnabled = false
        binding.mainChart.axisRight.isInverted = true
        binding.mainChart.setScaleEnabled(false)
        binding.mainChart.setVisibleYRangeMaximum(10F, YAxis.AxisDependency.LEFT);
        binding.mainChart.setPinchZoom(false)
    }
}