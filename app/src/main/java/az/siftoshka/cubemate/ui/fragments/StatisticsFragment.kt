package az.siftoshka.cubemate.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import az.siftoshka.cubemate.R
import az.siftoshka.cubemate.adapters.StatAdapter
import az.siftoshka.cubemate.db.Result
import az.siftoshka.cubemate.ui.viewmodels.MainViewModel
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.cToolbar
import kotlinx.android.synthetic.main.fragment_statistics.*


@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var statAdapter: StatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cToolbar.setExpandedTitleTextAppearance(R.style.CollapsingExpanded)
        cToolbar.setCollapsedTitleTextAppearance(R.style.CollapsingCollapsed)
        setupRecyclerView()
        observeAverageResult()
        val prefs = requireContext().getSharedPreferences("Sort-Type", Context.MODE_PRIVATE)
        when (prefs.getInt("Type Pos", 0)) {
            0 -> sortByDate()
            1 -> sortByTime()
            2 -> sortByType()
            else -> sortByDate()
        }

    }

    private fun observeAverageResult() = viewModel.avgResult.observe(viewLifecycleOwner, Observer {
        avgScore?.text = it?.toString()
        if (it == null) {
            avgScore?.textSize = 14F
            avgScore?.setTextColor(resources.getColor(R.color.gray))
            avgScore?.text = getString(R.string.avg_error)
        }
    })

    private fun setupRecyclerView() = recyclerView.apply {
        statAdapter = StatAdapter(object : StatAdapter.StatItemClickListener {
            override fun onPostClicked(result: Result) {
                openDialog(result)
            }
        })
        adapter = statAdapter
        layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun openDialog(result: Result) {
        val builder =
            MaterialAlertDialogBuilder(requireContext(), R.style.AppCompatAlertDialogStyle)
        builder.setTitle("Delete Result")
        builder.setMessage("Are you sure?")
        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            viewModel.deleteResult(result)
        }
        builder.setNegativeButton(android.R.string.no) { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun sortByDate() {
        viewModel.resultsByDate.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) emptyLayout.visibility = View.VISIBLE
            statAdapter.statisticList(it)
            setBarChart(it)
        })
    }

    private fun sortByTime() {
        viewModel.resultsByTime.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) emptyLayout.visibility = View.VISIBLE
            statAdapter.statisticList(it)
            setBarChart(it)
        })
    }

    private fun sortByType() {
        viewModel.resultsByType.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) emptyLayout.visibility = View.VISIBLE
            statAdapter.statisticList(it)
            setBarChart(it)
        })
    }

    private fun setBarChart(results: List<Result>) {
        val entries = ArrayList<BarEntry>()
        results.asReversed().forEachIndexed { index, result ->
            entries.add(BarEntry(index.toFloat(), result.timeInSeconds))
        }
        if(entries.isEmpty()) {
            dataEmptyText.visibility = View.VISIBLE
            mainChart.visibility = View.GONE
        }
        else {
            dataEmptyText.visibility = View.GONE
            mainChart.visibility = View.VISIBLE
        }
        val barDataSet = BarDataSet(entries.takeLast(30), "")
        barDataSet.setDrawValues(false)
        barDataSet.color = resources.getColor(R.color.colorPrimary)
        mainChart.data = BarData(barDataSet)
        mainChart.animateY(1000)
        mainChart.legend.isEnabled = false
        mainChart.axisRight.apply {
            setDrawGridLines(false)
            setDrawLabels(false)
            setDrawAxisLine(false)
        }
        mainChart.axisLeft.apply {
            setDrawGridLines(false)
            setDrawLabels(false)
            setDrawAxisLine(false)
        }
        mainChart.xAxis.apply {
            setDrawGridLines(false)
            setDrawLabels(false)
            setDrawAxisLine(false)
        }
        mainChart.data.isHighlightEnabled = false
        mainChart.setDrawGridBackground(false)
        mainChart.setDrawBorders(false)
        mainChart.description.isEnabled = false
        mainChart.axisRight.isInverted = true
        mainChart.setScaleEnabled(false)
        mainChart.setVisibleYRangeMaximum(10F, YAxis.AxisDependency.LEFT);
        mainChart.setPinchZoom(false)
    }
}