package com.asidarau.wtracker.presentation.ui

import androidx.recyclerview.widget.SortedListAdapterCallback
import com.asidarau.wtracker.domain.model.BaseModel
import com.asidarau.wtracker.domain.model.WeatherDto
import com.asidarau.wtracker.domain.model.WeatherErrorDto


/**
 *
 * @author Anton Sidorov on 24.11.2018.
 */
class WeatherSortedListAdapterCallback(
    adapter: WeatherAdapter
) : SortedListAdapterCallback<BaseModel>(adapter) {
    override fun areItemsTheSame(item1: BaseModel?, item2: BaseModel?): Boolean {
        return if (item1?.id == item2?.id) {
            return item1?.javaClass == item2?.javaClass
        } else false
    }

    override fun compare(o1: BaseModel?, o2: BaseModel?) = compareAdapterItems(o1, o2)


    override fun areContentsTheSame(oldItem: BaseModel?, newItem: BaseModel?): Boolean {
        //for simplicity omit full check on equality
        return compareAdapterItems(oldItem, newItem) == 0
    }

    private fun compareAdapterItems(o1: BaseModel?, o2: BaseModel?): Int {
        if (o1 is WeatherDto) {
            return if (o2 is WeatherDto) {
                o1.nowDate.compareTo(o2.nowDate)
            } else {
                val o2Error = o2 as WeatherErrorDto
                val result = o1.nowDate.compareTo(o2Error.date)
                if (result == 0) 1 else result
            }
        } else {
            val o1Error = o1 as WeatherErrorDto
            return if (o2 is WeatherDto) {
                val result = o1Error.date.compareTo(o2.nowDate)
                if (result == 0) 1 else result
            } else {
                val o2Error = o2 as WeatherErrorDto
                o1Error.date.compareTo(o2Error.date)
            }
        }
    }

}
