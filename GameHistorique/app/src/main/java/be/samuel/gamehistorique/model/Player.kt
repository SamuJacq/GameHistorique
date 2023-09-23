package be.samuel.gamehistorique.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
class Player (
    @PrimaryKey val idPlayer : UUID,
    val name : String,
    val score : Int,
    val idParty : UUID
)