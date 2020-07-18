package az.siftoshka.cubemate.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import az.siftoshka.cubemate.R
import az.siftoshka.cubemate.db.Result
import az.siftoshka.cubemate.utils.TypeConverter
import java.text.SimpleDateFormat
import kotlinx.android.synthetic.main.item_stat.view.*
import java.util.*

class StatAdapter : RecyclerView.Adapter<StatAdapter.StatViewHolder>() {

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
        return StatViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_stat,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return diff.currentList.size
    }

    override fun onBindViewHolder(holder: StatViewHolder, position: Int) {
        val stat = diff.currentList[position]
        holder.itemView.apply {
            val calendar = Calendar.getInstance().apply {
                timeInMillis = stat.timestamp
            }
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            dateText.text = dateFormat.format(calendar.time)

            scoreText.text = "${stat.timeInSeconds}"
            typeText.text = TypeConverter.convertToString(stat.type)
        }
    }
}