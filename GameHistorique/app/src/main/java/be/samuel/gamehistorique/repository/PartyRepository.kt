package be.samuel.gamehistorique.repository

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import be.samuel.gamehistorique.database.GameHistoriqueDatabase
import be.samuel.gamehistorique.model.Game
import be.samuel.gamehistorique.model.Party
import be.samuel.gamehistorique.model.Player
import kotlinx.coroutines.flow.Flow
import java.util.*

const val DATABASE_NAME = "database"

class PartyRepository private constructor(context: Context){

    val TABLE_GAME = "CREATE TABLE GAME(" +
            "name TEXT NOT NULL," +
            "description TEXT NOT NULL," +
            "numberPlayer INTEGER NOT NULL," +
            "photo TEXT NOT NULL," +
            "idGame TEXT PRIMARY KEY NOT NULL," +
            "time INTEGER NOT NULL)"
    val TABLE_PARTY = "CREATE TABLE PARTY(" +
            "idGame TEXT NOT NULL,"+
            "idParty TEXT PRIMARY KEY NOT NULL," +
            "latitude REAL NOT NULL," +
            "name TEXT NOT NULL," +
            "description TEXT NOT NULL," +
            "photo TEXT NOT NULL,"+
            "time INTEGER NOT NULL," +
            "longitude REAL NOT NULL)"

    val TABLE_PLAYER = "CREATE TABLE PLAYER(" +
            "name TEXT NOT NULL," +
            "score INTEGER NOT NULL," +
            "idPlayer TEXT PRIMARY KEY NOT NULL," +
            "idParty TEXT NOT NULL)"

    class Migration2To3 : Migration(4, 5) {

        val TABLE_GAME = "CREATE TABLE GAME(" +
                "name TEXT NOT NULL," +
                "description TEXT NOT NULL," +
                "numberPlayer INTEGER NOT NULL," +
                "photo TEXT NOT NULL," +
                "idGame TEXT PRIMARY KEY NOT NULL," +
                "time INTEGER NOT NULL)"
        val TABLE_PARTY = "CREATE TABLE PARTY(" +
                "idGame TEXT NOT NULL,"+
                "idParty TEXT PRIMARY KEY NOT NULL," +
                "latitude REAL NOT NULL," +
                "name TEXT NOT NULL," +
                "description TEXT NOT NULL," +
                "photo TEXT NOT NULL,"+
                "time INTEGER NOT NULL," +
                "longitude REAL NOT NULL)"

        val TABLE_PLAYER = "CREATE TABLE PLAYER(" +
                "name TEXT NOT NULL," +
                "score INTEGER NOT NULL," +
                "idPlayer TEXT PRIMARY KEY NOT NULL," +
                "idParty TEXT NOT NULL)"
        override fun migrate(db: SupportSQLiteDatabase) {

            db.execSQL("DROP TABLE PLAYER")
            db.execSQL("DROP TABLE PARTY")
            db.execSQL("DROP TABLE GAME")

            db.execSQL(TABLE_GAME)
            db.execSQL(TABLE_PARTY)
            db.execSQL(TABLE_PLAYER)

            val idGame1 = UUID.randomUUID()
            val idGame2 = UUID.randomUUID()
            val idGame3 = UUID.randomUUID()
            val idParty1 = UUID.randomUUID()
            val idParty2 = UUID.randomUUID()
            val idParty3 = UUID.randomUUID()
            val idParty4 = UUID.randomUUID()

            db.execSQL("DELETE FROM PLAYER")
            db.execSQL("DELETE FROM PARTY")
            db.execSQL("DELETE FROM GAME")
                                                    //name, description, number, photo, id, time
            db.execSQL("INSERT INTO GAME VALUES('échec', 'jeu de plateau', 2, '', '$idGame1', 20)")
            db.execSQL("INSERT INTO GAME VALUES('UNO', 'jeu de carte', 4, '','$idGame2', 15)")
            db.execSQL("INSERT INTO GAME VALUES('Jeu de l oie', 'jeu de plateau avec des dé', 6, '', '$idGame3', 30)")
                                                    //idGame, idParty, latitude, name, description, photo, time, longitude
            db.execSQL("INSERT INTO PARTY VALUES('$idGame1', '$idParty1', 5.5732, 'échec avec mon père', 'c était une partie assez long', '', 18, 50.6223)")
            db.execSQL("INSERT INTO PARTY VALUES('$idGame2', '$idParty2', 5.5732, 'UNO avec ma famille', 'on s est bien amusé', '', 16, 50.6223)")
            db.execSQL("INSERT INTO PARTY VALUES('$idGame1', '$idParty3', 5.5732, 'echec avec mon père', 'c était une partie très fun', '', 12, 50.6223)")
            db.execSQL("INSERT INTO PARTY VALUES('$idGame3', '$idParty4', 5.5732, 'l oie dans son lac', 'partie avec ma famille et mon cousin', '', 27, 50.6223)")
                                                    //name, score, idPlayer, idParty
            db.execSQL("INSERT INTO PLAYER VALUES('Samuel', 20, '${UUID.randomUUID()}', '$idParty1')")
            db.execSQL("INSERT INTO PLAYER VALUES('Thierry', 22, '${UUID.randomUUID()}', '$idParty1')")
            db.execSQL("INSERT INTO PLAYER VALUES('Samuel', 4, '${UUID.randomUUID()}',  '$idParty2')")
            db.execSQL("INSERT INTO PLAYER VALUES('Thierry', 1, '${UUID.randomUUID()}', '$idParty2')")
            db.execSQL("INSERT INTO PLAYER VALUES('Joëlle', 3, '${UUID.randomUUID()}', '$idParty2')")
            db.execSQL("INSERT INTO PLAYER VALUES('Florian', 2, '${UUID.randomUUID()}', '$idParty2')")
            db.execSQL("INSERT INTO PLAYER VALUES('Samuel', 24, '${UUID.randomUUID()}', '$idParty3')")
            db.execSQL("INSERT INTO PLAYER VALUES('Florian', 18, '${UUID.randomUUID()}', '$idParty3')")
            db.execSQL("INSERT INTO PLAYER VALUES('Samuel', 58, '${UUID.randomUUID()}', '$idParty4')")
            db.execSQL("INSERT INTO PLAYER VALUES('Thierry', 59, '${UUID.randomUUID()}', '$idParty4')")
            db.execSQL("INSERT INTO PLAYER VALUES('Joëlle', 37, '${UUID.randomUUID()}', '$idParty4')")
            db.execSQL("INSERT INTO PLAYER VALUES('Florian', 45, '${UUID.randomUUID()}', '$idParty4')")
            db.execSQL("INSERT INTO PLAYER VALUES('Tom', 63, '${UUID.randomUUID()}', '$idParty4')")
        }
    }

    val migration = Migration2To3()

    private val database : GameHistoriqueDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            GameHistoriqueDatabase::class.java,
            DATABASE_NAME).createFromAsset(DATABASE_NAME).allowMainThreadQueries().build()

   /*private val database : GameHistoriqueDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            GameHistoriqueDatabase::class.java,
            DATABASE_NAME).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                db.execSQL("DROP TABLE PLAYER")
                db.execSQL("DROP TABLE PARTY")
                db.execSQL("DROP TABLE GAME")

                db.execSQL(TABLE_GAME)
                db.execSQL(TABLE_PARTY)
                db.execSQL(TABLE_PLAYER)

                val idGame1 = UUID.randomUUID()
                val idGame2 = UUID.randomUUID()
                val idGame3 = UUID.randomUUID()
                val idParty1 = UUID.randomUUID()
                val idParty2 = UUID.randomUUID()
                val idParty3 = UUID.randomUUID()
                val idParty4 = UUID.randomUUID()

                db.execSQL("DELETE FROM PLAYER")
                db.execSQL("DELETE FROM PARTY")
                db.execSQL("DELETE FROM GAME")
                //name, description, number, photo, id, time
                db.execSQL("INSERT INTO GAME VALUES('échec', 'jeu de plateau', 2, '', '$idGame1', 20)")
                db.execSQL("INSERT INTO GAME VALUES('UNO', 'jeu de carte', 4, '','$idGame2', 15)")
                db.execSQL("INSERT INTO GAME VALUES('Jeu de l oie', 'jeu de plateau avec des dé', 6, '', '$idGame3', 30)")
                //idGame, idParty, latitude, name, description, photo, time, longitude
                db.execSQL("INSERT INTO PARTY VALUES('$idGame1', '$idParty1', 5.5732, 'échec avec mon père', 'c était une partie assez long', '', 18, 50.6223)")
                db.execSQL("INSERT INTO PARTY VALUES('$idGame2', '$idParty2', 5.5732, 'UNO avec ma famille', 'on s est bien amusé', '', 16, 50.6223)")
                db.execSQL("INSERT INTO PARTY VALUES('$idGame1', '$idParty3', 5.5732, 'echec avec mon père', 'c était une partie très fun', '', 12, 50.6223)")
                db.execSQL("INSERT INTO PARTY VALUES('$idGame3', '$idParty4', 5.5732, 'l oie dans son lac', 'partie avec ma famille et mon cousin', '', 27, 50.6223)")
                //name, score, idPlayer, idParty
                db.execSQL("INSERT INTO PLAYER VALUES('Samuel', 20, '${UUID.randomUUID()}', '$idParty1')")
                db.execSQL("INSERT INTO PLAYER VALUES('Thierry', 22, '${UUID.randomUUID()}', '$idParty1')")
                db.execSQL("INSERT INTO PLAYER VALUES('Samuel', 4, '${UUID.randomUUID()}',  '$idParty2')")
                db.execSQL("INSERT INTO PLAYER VALUES('Thierry', 1, '${UUID.randomUUID()}', '$idParty2')")
                db.execSQL("INSERT INTO PLAYER VALUES('Joëlle', 3, '${UUID.randomUUID()}', '$idParty2')")
                db.execSQL("INSERT INTO PLAYER VALUES('Florian', 2, '${UUID.randomUUID()}', '$idParty2')")
                db.execSQL("INSERT INTO PLAYER VALUES('Samuel', 24, '${UUID.randomUUID()}', '$idParty3')")
                db.execSQL("INSERT INTO PLAYER VALUES('Florian', 18, '${UUID.randomUUID()}', '$idParty3')")
                db.execSQL("INSERT INTO PLAYER VALUES('Samuel', 58, '${UUID.randomUUID()}', '$idParty4')")
                db.execSQL("INSERT INTO PLAYER VALUES('Thierry', 59, '${UUID.randomUUID()}', '$idParty4')")
                db.execSQL("INSERT INTO PLAYER VALUES('Joëlle', 37, '${UUID.randomUUID()}', '$idParty4')")
                db.execSQL("INSERT INTO PLAYER VALUES('Florian', 45, '${UUID.randomUUID()}', '$idParty4')")
                db.execSQL("INSERT INTO PLAYER VALUES('Tom', 63, '${UUID.randomUUID()}', '$idParty4')")
            }
        }).allowMainThreadQueries().build()*/
    fun getPartys() : Flow<List<Party>> = database.partyDao().getPartys()

    fun getPlayers() : Flow<List<Player>> = database.partyDao().getPlayers()

    fun getGames() : Flow<List<Game>> = database.partyDao().getGames()

    suspend fun getPartyWithGame(idGame : UUID) : List<Party> = database.partyDao().getPartyWithGame(idGame)

    fun getPartyById(idParty : UUID) : Party = database.partyDao().getPartyById(idParty)

    fun getGameById(idGame : UUID) : Game = database.partyDao().getGameById(idGame)
    fun getGameByName(nameGame : String) : UUID = database.partyDao().getGameByName(nameGame)

    fun getPlayersByParty(idParty : UUID) : List<Player> = database.partyDao().getPlayersByParty(idParty)

    fun insertParty(party : Party) = database.partyDao().insertParty(party)
    fun insertPlayer(player : Player) = database.partyDao().insertPlayer(player)

    fun insertGame(game: Game) = database.partyDao().insertGame(game)

    fun updateParty(party: Party) = database.partyDao().updateParty(party)

    companion object{
        private var INSTANCE : PartyRepository? = null

        fun initialize(context: Context){
            if(INSTANCE == null){
                INSTANCE = PartyRepository(context)
            }
        }

        fun get() : PartyRepository{
            return INSTANCE ?: throw IllegalStateException("PartyRepository must be initialized")
        }
    }

}