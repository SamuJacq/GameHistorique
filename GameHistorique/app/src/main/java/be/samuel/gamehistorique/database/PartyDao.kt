package be.samuel.gamehistorique.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import be.samuel.gamehistorique.model.Game
import be.samuel.gamehistorique.model.Party
import be.samuel.gamehistorique.model.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

@Dao
interface PartyDao {
    @Query("SELECT * FROM party")
    fun getPartys() : Flow<List<Party>>
    @Query("SELECT * FROM game")
    fun getGames() : Flow<List<Game>>

    @Query("SELECT * FROM player")
    fun getPlayers() : Flow<List<Player>>

    @Query("SELECT * FROM party WHERE idGame=(:idGame)")
    suspend fun getPartyWithGame(idGame : UUID) : List<Party>

    @Query("SELECT * FROM party WHERE idParty=(:idParty)")
    fun getPartyById(idParty : UUID) : Party

    @Query("SELECT * FROM game WHERE idGame=(:idGame)")
    fun getGameById(idGame : UUID) : Game

    @Query("SELECT idGame FROM game WHERE name=(:nameGame)")
    fun getGameByName(nameGame : String) : UUID

    @Query("SELECT * FROM player WHERE idParty=(:idParty)")
    fun getPlayersByParty(idParty : UUID) : List<Player>

    @Insert
    fun insertParty(vararg party:Party)

    @Insert
    fun insertPlayer(vararg player:Player)

    @Insert
    fun insertGame(vararg game: Game)

    @Update
    fun updateParty(vararg party:Party)

}