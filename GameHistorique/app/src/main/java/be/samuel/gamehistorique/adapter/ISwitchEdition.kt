package be.samuel.gamehistorique.adapter

import java.util.*

interface ISwitchEdition {

    fun switchAccueil()

    fun switchAddGame()

    fun switchAddParty(idParty: UUID?)

    fun switchUpdateParty(idParty : UUID)

    fun switchAddPlayer(idParty : UUID)

}