package be.samuel.gamehistorique.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.samuel.gamehistorique.model.Game
import be.samuel.gamehistorique.model.Party
import be.samuel.gamehistorique.model.Player
import be.samuel.gamehistorique.repository.PartyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class CreatePartyViewModel: ViewModel(){

    private val partyRepository = PartyRepository.get()
    private var partyUpdate :Party = Party(UUID.randomUUID(), "game 5", "description 5", 18, 0.0, 0.0,"", UUID.randomUUID())

    fun createParty(nameGame: String, nameParty: String, timeParty: Int, description: String, longitude: Double, latitude: Double, photo : String) {
        val party = Party(UUID.randomUUID(), nameParty, description, timeParty, longitude, latitude, photo, partyRepository.getGameByName(nameGame))
        partyRepository.insertParty(party)
    }

    fun existGame(nameGame : String) : Boolean{
        return partyRepository.getGameByName(nameGame) != null
    }

    fun getPartyUpdate(idParty : UUID) : PartyView{
        partyUpdate = partyRepository.getPartyById(idParty)
        val game = partyRepository.getGameById(partyUpdate.idGame)
        return PartyView(partyUpdate.name, partyUpdate.description, partyUpdate.time, 0, partyUpdate.longitude, partyUpdate.latitude, partyUpdate.photo, game.name, partyUpdate.photo)
    }


}