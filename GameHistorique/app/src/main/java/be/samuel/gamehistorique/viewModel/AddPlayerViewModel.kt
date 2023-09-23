package be.samuel.gamehistorique.viewModel

import androidx.lifecycle.ViewModel
import be.samuel.gamehistorique.model.Player
import be.samuel.gamehistorique.repository.PartyRepository
import java.util.*

class AddPlayerViewModel : ViewModel() {

    private val repository = PartyRepository.get()

    fun addPlayer(idParty : UUID, namePlayer : String, score : Int){
        val player = Player(UUID.randomUUID(), namePlayer, score, idParty)
        repository.insertPlayer(player)
    }

}