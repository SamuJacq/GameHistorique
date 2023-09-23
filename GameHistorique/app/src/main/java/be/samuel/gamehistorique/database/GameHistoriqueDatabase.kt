package be.samuel.gamehistorique.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import be.samuel.gamehistorique.model.Party
import be.samuel.gamehistorique.model.Player
import be.samuel.gamehistorique.model.Game

@Database(entities = [Party::class, Player::class, Game::class], version=5)
@TypeConverters(PartyTypeConverters::class)
abstract class GameHistoriqueDatabase : RoomDatabase() {
    abstract fun partyDao() : PartyDao
}