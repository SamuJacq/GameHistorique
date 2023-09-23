package be.samuel.gamehistorique.database

import androidx.room.TypeConverter
import java.util.*

class PartyTypeConverters {

    @TypeConverter
    fun fromUUID(uuid : UUID) : String = uuid.toString()


    @TypeConverter
    fun toUUID(uuid : String) : UUID = UUID.fromString(uuid)

}