package be.samuel.gamehistorique.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
class Game (
    @PrimaryKey val idGame : UUID,
    val name : String,
    val description : String,
    val numberPlayer : Int,
    val time : Int,
    val photo : String
)