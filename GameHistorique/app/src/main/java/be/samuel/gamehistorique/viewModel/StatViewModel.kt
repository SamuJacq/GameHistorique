package be.samuel.gamehistorique.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.samuel.gamehistorique.adapter.StatPlayerListAdapter
import be.samuel.gamehistorique.adapter.StatTimeListAdapter
import be.samuel.gamehistorique.model.Party
import be.samuel.gamehistorique.model.Player
import be.samuel.gamehistorique.repository.PartyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class StatViewModel(private val partyRepository : PartyRepository) : ViewModel() {

    //private val partyRepository = PartyRepository.get()

    private val _partys : MutableStateFlow<List<Party>> = MutableStateFlow(emptyList())
    private val _players: MutableStateFlow<List<Player>> = MutableStateFlow(emptyList())

    val partys: StateFlow<List<Party>> get() = _partys.asStateFlow()
    val players: StateFlow<List<Player>> get() = _players.asStateFlow()

    private var gameCurrent : UUID? = null
    lateinit var partyCurrent : List<Party>

    init{
        viewModelScope.launch {
            partyRepository.getPlayers().collect{
                _players.value = it
            }
        }
        viewModelScope.launch {
            partyRepository.getPartys().collect {
                _partys.value = it
            }
        }
    }

    fun gameSelected(nameGame : String) : Boolean{
        val name = partyRepository.getGameByName(nameGame)
        if(name != null){
            gameCurrent = name
            partysByGame()
        }
        return gameCurrent != null
    }

    fun getAdapterTime() : StatTimeListAdapter = StatTimeListAdapter(partyCurrent,listPlayerByParty())


    fun getAdapterPlayer() : StatPlayerListAdapter = StatPlayerListAdapter(listPlayerByParty())

    fun numberParty() : Int{
        var numberParty = 0
        for(party in partys.value){
            if(party.idGame == gameCurrent)
                numberParty++
        }

        return numberParty
    }

    private fun partysByGame() : List<Party>{
        var list : List<Party> = listOf()
        for(party in partys.value){
            if(party.idGame == gameCurrent)
                list += party
        }
        partyCurrent = list
        return list
    }

    private fun listPlayerByParty() : List<Player>{
        var listPlayer : List<Player> = listOf()
        for(party in partyCurrent){
            for(player in players.value){
                if(player.idParty == party.idParty)
                    listPlayer += player
            }
        }
        return listPlayer
    }

}