package com.asidarau.wtracker.presentation.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import com.asidarau.wtracker.R
import com.asidarau.wtracker.domain.model.BaseModel
import com.asidarau.wtracker.domain.model.WeatherDto
import com.asidarau.wtracker.domain.model.WeatherErrorDto
import com.asidarau.wtracker.presentation.inflate
import kotlinx.android.synthetic.main.layout_weather_error_item.view.*
import kotlinx.android.synthetic.main.layout_weather_item.view.*


/**
 *
 * @author Anton Sidorov on 22.11.2018.
 */
class WeatherAdapter(
    private val listener: (Long) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items: SortedList<BaseModel> = SortedList(
        BaseModel::class.java,
        WeatherSortedListAdapterCallback(this)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.layout_weather_item -> ForecastHolder(parent.inflate(viewType))
            else -> ErrorHolder(parent.inflate(viewType))
        }
    }

    override fun getItemCount() = items.size()

    fun setItems(data: List<BaseModel>) {
        with(items) {
            clear()
            addAll(data)
        }
        notifyDataSetChanged()
    }

    fun addItems(data: List<BaseModel>) {
        val oldSize = itemCount
        items.addAll(data)
        notifyItemRangeInserted(oldSize, data.size)
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return when (item) {
            is WeatherDto -> R.layout.layout_weather_item
            else -> R.layout.layout_weather_error_item
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(position)
        if (type == R.layout.layout_weather_item) {
            val forecastHolder = holder as ForecastHolder
            forecastHolder.bind(items[position] as WeatherDto, listener)
        } else {
            val errorHolder = holder as ErrorHolder
            errorHolder.bind(items[position] as WeatherErrorDto)
        }
    }
}

class ForecastHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(weather: WeatherDto, listener: (Long) -> Unit) {
        with(itemView) {
            tvDate.text = weather.forecast.date
            tvTemp.text = weather.fact.temperature.toString()
            setOnClickListener {
                weather.id?.let(listener)
            }
        }
    }
}

class ErrorHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(error: WeatherErrorDto) {
        with(itemView) {
            tvErrorDate.text = error.date.toString()
            tvError.text = error.errorName
        }
    }
}
