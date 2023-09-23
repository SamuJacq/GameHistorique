package be.samuel.gamehistorique.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
class Party (
    @PrimaryKey val idParty : UUID,
    val name : String,
    val description : String,
    val time : Int,
    val longitude : Double,
    val latitude : Double,
    val photo : String,
    val idGame : UUID
)