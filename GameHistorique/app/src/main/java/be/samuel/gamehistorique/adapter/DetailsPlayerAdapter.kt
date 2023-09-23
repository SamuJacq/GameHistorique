package be.samuel.gamehistorique.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import be.samuel.gamehistorique.databinding.ActivityStatItemPlayerBinding
import be.samuel.gamehistorique.model.Player

class DetailsPlayerAdapter(
    private val players: List<Player>,
    private val highScore : Int
) : RecyclerView.Adapter<DetailPlayerHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : DetailPlayerHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ActivityStatItemPlayerBinding.inflate(inflater, parent, false)

        return DetailPlayerHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailPlayerHolder, position: Int) {
        val player = players[position]
        val winner = if (player.score == highScore)  "(gagnant)" else ""
        holder.bind(player, winner)
    }

    override fun getItemCount() = players.size
}

class DetailPlayerHolder(val binding: ActivityStatItemPlayerBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(player : Player, winner : String){
        binding.playerItem.text = "${player.name}, score = ${player.score} $winner"
    }
}