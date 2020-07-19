package az.siftoshka.cubemate.ui.fragments

import android.app.AlertDialog
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

        viewModel.resultsByTime.observe(viewLifecycleOwner, Observer {
            statAdapter.statisticList(it)
        })
    }

    private fun observeAverageResult() = viewModel.avgResult.observe(viewLifecycleOwner, Observer {
        avgScore.text = it.toString()
    })

    private fun setupRecyclerView() = recyclerView.apply {
        statAdapter = StatAdapter (object : StatAdapter.StatItemClickListener {
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
        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.AppCompatAlertDialogStyle)
        builder.setTitle("Delete Result")
        builder.setMessage("Are you sure?")
        builder.setPositiveButton(android.R.string.yes) { _, _ ->
            viewModel.deleteResult(result)
        }
        builder.setNegativeButton(android.R.string.no) { dialog, _ -> dialog.dismiss() }
        builder.show()
    }
}