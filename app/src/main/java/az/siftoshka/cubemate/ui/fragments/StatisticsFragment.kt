package az.siftoshka.cubemate.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import az.siftoshka.cubemate.R
import az.siftoshka.cubemate.adapters.StatAdapter
import az.siftoshka.cubemate.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.*
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
    }

    private fun setupRecyclerView() = recyclerView.apply {
        statAdapter = StatAdapter()
        adapter = statAdapter
        layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }

}