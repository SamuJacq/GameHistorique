package be.samuel.gamehistorique.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import be.samuel.gamehistorique.model.Game
import be.samuel.gamehistorique.repository.PartyRepository
import java.util.*

class CreateGameViewModel : ViewModel(){

    private val partyRepository = PartyRepository.get()

    fun createGame(nameGame: String, description: String, numberPlayer: Int, timeParty: Int, photo: String) {
        val game = Game(UUID.randomUUID(), nameGame, description, numberPlayer, timeParty, photo)
        partyRepository.insertGame(game)
    }

}