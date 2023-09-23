package be.samuel.gamehistorique.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import be.samuel.gamehistorique.databinding.ActivityStatItemPlayerBinding
import be.samuel.gamehistorique.model.Player
import java.util.*

class StatPlayerListAdapter (
    private val players: List<Player>
) : RecyclerView.Adapter<StatPlayerHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : StatPlayerHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ActivityStatItemPlayerBinding.inflate(inflater, parent, false)

        return StatPlayerHolder(binding)
    }

    override fun onBindViewHolder(holder: StatPlayerHolder, position: Int) {
        holder.bind(players[position].name, position+1)
    }

    override fun getItemCount() = players.size

}

class StatPlayerHolder(val binding: ActivityStatItemPlayerBinding) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(namePlayer : String, puce : Int) {
        binding.playerItem.text = "$puce. $namePlayer"
    }
}