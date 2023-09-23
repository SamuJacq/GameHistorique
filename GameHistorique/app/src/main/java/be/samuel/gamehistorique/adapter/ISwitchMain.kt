package be.samuel.gamehistorique.adapter

import java.util.*

interface ISwitchMain {

    fun switchDetail(idParty : UUID)

    fun switchAddPlayer(idParty : UUID)

    fun switchStat()

    fun switchEdition()

}