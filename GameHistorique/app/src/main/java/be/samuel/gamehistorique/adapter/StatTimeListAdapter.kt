package be.samuel.gamehistorique.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import be.samuel.gamehistorique.databinding.ActivityStatItemAverageTimeBinding
import be.samuel.gamehistorique.databinding.ActivityStatItemPlayerBinding
import be.samuel.gamehistorique.model.Party
import be.samuel.gamehistorique.model.Player
import java.util.*

class StatTimeListAdapter (
    private val partys: List<Party>,
    private val players: List<Player>
) : RecyclerView.Adapter<StatTimeHolder>(){

    //private var averageSave : List<Int> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : StatTimeHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ActivityStatItemAverageTimeBinding.inflate(inflater, parent, false)

        return StatTimeHolder(binding)
    }

    override fun onBindViewHolder(holder: StatTimeHolder, position: Int) {
        val party = partys[position]
        val numberPlayer = numberPlayer(party.idParty)
        var averageTime = party.time

        for(p in partys){
            if(p.idParty != party.idParty && numberPlayer(p.idParty) == numberPlayer)
                averageTime += p.time
        }
        if(numberPlayer != 0/*&& !averageSave.contains(numberPlayer)*/){
            //averageSave += numberPlayer
            holder.bind(averageTime/numberPlayer, numberPlayer)
        }
    }

    override fun getItemCount() = partys.size

    private fun numberPlayer(idParty :UUID) : Int{
        var numberPlayer = 0
        for(player in players){
            if(player.idParty == idParty)
                numberPlayer++
        }
        return numberPlayer
    }

}

    class StatTimeHolder(val binding: ActivityStatItemAverageTimeBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(time : Int, numberPlayer : Int) {
            binding.averageTime.text = "durée moyenne pour $numberPlayer joueurs : $time min"
        }
    }