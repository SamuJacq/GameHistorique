package be.samuel.gamehistorique.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.samuel.gamehistorique.adapter.DetailsPlayerAdapter
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

class DetailsPartyViewModel(private val partyRepository : PartyRepository) : ViewModel(){

    //private val partyRepository = PartyRepository.get()
    private var party : Party = Party(UUID.randomUUID(), "game 5", "description 5", 18, 0.0, 0.0, "", UUID.randomUUID())
    private var game : Game = Game(UUID.randomUUID(), "game 5", "description 5", 0, 0, "")
    private lateinit var players : List<Player>

    fun initParty(idParty : UUID) : PartyView{
        runBlocking{
            party = partyRepository.getPartyById(idParty)
            players = partyRepository.getPlayersByParty(idParty)
            game = partyRepository.getGameById(party.idGame)
        }
        return PartyView(party.name, party.description, party.time,players.size, party.longitude, party.latitude, party.photo, game.name, game.photo)
    }

    fun getAdapter() : DetailsPlayerAdapter{
        return DetailsPlayerAdapter(players, getHighScore())
    }

    private fun getHighScore() : Int{
        var highScore : Int = 0
        for(player in players){
            if(player.score > highScore){
                highScore = player.score
            }
        }
        return highScore
    }

}