package be.samuel.gamehistorique.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.samuel.gamehistorique.model.Game
import be.samuel.gamehistorique.model.Party
import be.samuel.gamehistorique.model.Player
import be.samuel.gamehistorique.repository.PartyRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel : ViewModel(){

    private val partyRepository = PartyRepository.get()

     private val _partys : MutableStateFlow<List<Party>> = MutableStateFlow(emptyList())
     private val _players: MutableStateFlow<List<Player>> = MutableStateFlow(emptyList())
     private val _games: MutableStateFlow<List<Game>> = MutableStateFlow(emptyList())

    val partys: StateFlow<List<Party>> get() = _partys.asStateFlow()
    val players: StateFlow<List<Player>> get() = _players.asStateFlow()
    val games: StateFlow<List<Game>> get() = _games.asStateFlow()

    fun init() {
        viewModelScope.launch {
            partyRepository.getPlayers().collect{
                _players.value = it
            }
        }
        viewModelScope.launch {
            partyRepository.getGames().collect {
                _games.value = it
            }
        }
        viewModelScope.launch {
            delay(200)
            partyRepository.getPartys().collect {
                _partys.value = it
            }
        }

    }

    fun getPlayer() : List<Player>{
        return players.value
    }

    fun getCurrentId(idParty : UUID) : UUID{
        for(party in partys.value){
            if(party.idParty == idParty){
                return party.idParty
            }
        }
        return UUID.randomUUID()
    }

    fun getGameNameSelect(position : Int) : UUID{
        return games.value[position].idGame
    }

    fun getPartyFilter(idGame : UUID?, numberPlayer : Int) : List<Party>{
        var list : List<Party> = listOf()
        var number: Int
        for(party in partys.value){
            number = 0
            for(player in players.value){
                if (player.idParty == party.idParty){
                    number++
                }
            }
            if((idGame == null || idGame == party.idGame) && (numberPlayer == -1 || numberPlayer == number)){
                list += party
            }
        }
        return list
    }

}