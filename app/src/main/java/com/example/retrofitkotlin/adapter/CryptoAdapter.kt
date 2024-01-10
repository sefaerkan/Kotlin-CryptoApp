package com.example.retrofitkotlin.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitkotlin.databinding.RowLayoutBinding
import com.example.retrofitkotlin.model.CryptoModel

class CryptoAdapter(private val cryptoList : ArrayList<CryptoModel>) : RecyclerView.Adapter<CryptoAdapter.RowHolder>() {

    private val colors: Array<String> = arrayOf("#772233","#fcb994","#d7e2f3","#0ab653","#ff0000","#395463","#ff7f00","#efc0cd")

    class RowHolder(val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cryptoModel: CryptoModel, colors: Array<String>, position: Int) {
            binding.name.text = cryptoModel.currency
            binding.price.text = cryptoModel.price

            binding.root.setBackgroundColor(Color.parseColor(colors[position % 8]))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val binding = RowLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RowHolder(binding)
    }

    override fun getItemCount(): Int {
        return cryptoList.count()
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        holder.bind(cryptoList[position],colors,position)
    }

}