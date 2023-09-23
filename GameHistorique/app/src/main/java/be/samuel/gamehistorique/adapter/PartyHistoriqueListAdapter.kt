package be.samuel.gamehistorique.adapter

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import be.samuel.gamehistorique.databinding.ActivityHistoriqueItemBinding
import be.samuel.gamehistorique.model.Party
import be.samuel.gamehistorique.model.Player
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class PartyHistoriqueListAdapter(
    private val partys: List<Party>,
    private val players: List<Player>,
    private val interfaceDetails: ISwitchMain,
) : RecyclerView.Adapter<PartyHistoriqueHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int) : PartyHistoriqueHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ActivityHistoriqueItemBinding.inflate(inflater, parent, false)

        return PartyHistoriqueHolder(binding)
    }

    override fun onBindViewHolder(holder: PartyHistoriqueHolder, position: Int) {
        val party = partys[position]
        var number = 0
        for(player in players){
            if (player.idParty == party.idParty){
                number++
            }
        }
        holder.bind(party, number, interfaceDetails)
    }

    override fun getItemCount() = partys.size

}

class PartyHistoriqueHolder(val binding: ActivityHistoriqueItemBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(party : Party, number : Int, interfaceDetails: ISwitchMain){

        binding.partyNameAccueil.text = party.name
        binding.numberPlayerAccueil.text = "nombre de joueur : $number"
        val bitmap = BitmapFactory.decodeFile(party.photo)
        if(bitmap != null){
            binding.imageGameHistorique.setImageBitmap(bitmap)
        }

        binding.buttonDetails.setOnClickListener{
            interfaceDetails.switchDetail(party.idParty)
        }

        binding.buttonAddPlayer.setOnClickListener{
            interfaceDetails.switchAddPlayer(party.idParty)
        }

    }
}