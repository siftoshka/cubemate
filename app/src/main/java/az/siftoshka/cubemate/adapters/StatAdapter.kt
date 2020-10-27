package az.siftoshka.cubemate.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import az.siftoshka.cubemate.R
import az.siftoshka.cubemate.db.Result
import az.siftoshka.cubemate.utils.Converter
import kotlinx.android.synthetic.main.item_stat.view.*
import java.text.SimpleDateFormat
import java.util.*


class StatAdapter(private val clickListener: StatItemClickListener) : RecyclerView.Adapter<StatAdapter.StatViewHolder>() {

    companion object {
        var mClickListener: StatItemClickListener? = null
    }

    interface StatItemClickListener {
        fun onPostClicked(result: Result)
    }

    inner class StatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffCallback = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val diff = AsyncListDiffer(this, diffCallback)

    fun statisticList(list: List<Result>) = diff.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatViewHolder {
        return StatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_stat, parent, false))
    }

    override fun getItemCount(): Int {
        return diff.currentList.size
    }

    override fun onBindViewHolder(holder: StatViewHolder, position: Int) {
        val stat = diff.currentList[position]
        holder.itemView.apply {
            dateText.text = Converter.timeToDate(stat.timestamp)
            scoreText.text = "${stat.timeInSeconds}"
            typeText.text = stat.type
            mClickListener = clickListener
            holder.itemView.setOnLongClickListener {
                mClickListener?.onPostClicked(stat)
                true
            }
        }
    }
}