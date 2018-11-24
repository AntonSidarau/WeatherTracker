package com.asidarau.wtracker.presentation.ui

import androidx.recyclerview.widget.DiffUtil
import com.asidarau.wtracker.domain.model.BaseModel


/**
 *
 * @author Anton Sidorov on 24.11.2018.
 */
class WeatherDiffUtil(
    private var oldList: List<BaseModel>,
    private val newList: List<BaseModel>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldModel = oldList[oldItemPosition]
        val newModel = newList[newItemPosition]
        return if (oldModel.id == oldModel.id) {
            return oldModel.javaClass == newModel.javaClass
        } else false
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldModel = oldList[oldItemPosition]
        val newModel = newList[newItemPosition]

        return oldModel.equals(newModel)
    }
}
