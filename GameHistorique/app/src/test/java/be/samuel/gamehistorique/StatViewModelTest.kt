package be.samuel.gamehistorique

import be.samuel.gamehistorique.model.Party
import be.samuel.gamehistorique.model.Player
import be.samuel.gamehistorique.repository.PartyRepository
import be.samuel.gamehistorique.viewModel.StatViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.*

class StatViewModelTest {

    private lateinit var mockedViewModel: StatViewModel
    private val mockPartyRepository = mockk<PartyRepository>()

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        every { mockPartyRepository.getGameByName("échec") } returns UUID.fromString("d3978703-9af4-48a6-b350-f37c99e0f902")
        every { mockPartyRepository.getPlayers() } returns flowOf(listOf(
            Player(UUID.randomUUID(), "player 1", 20, UUID.fromString("e3978703-9af4-48a6-b350-f37c99e0f902")),
            Player(UUID.randomUUID(), "player 2", 20, UUID.fromString("e3978703-9af4-48a6-b350-f37c99e0f902")),
            Player(UUID.randomUUID(), "player 3", 20, UUID.fromString("e3978703-9af4-48a6-b350-f37c99e0f902")),
            Player(UUID.randomUUID(), "player 4", 20, UUID.fromString("e3978703-9af4-48a6-b350-f37c99e0f902")),
        ))
        every { mockPartyRepository.getPartys() } returns flowOf(listOf(
            Party(UUID.fromString("e3978703-9af4-48a6-b350-f37c99e0f902"), "name 1", "description 1", 18, 0.0,0.0,"", UUID.fromString("d3978703-9af4-48a6-b350-f37c99e0f902")),
            Party(UUID.randomUUID(), "name 2", "description 2", 18, 0.0,0.0,"", UUID.fromString("d3978703-9af4-48a6-b350-f37c99e0f902")),
            Party(UUID.randomUUID(), "name 3", "description 3", 18, 0.0,0.0,"", UUID.fromString("d3978703-9af4-48a6-b350-f37c99e0f902")),
        ))

        mockedViewModel = StatViewModel(mockPartyRepository)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testGameExists(){
        val result = mockedViewModel.gameSelected("échec")
        assertTrue(result)
    }

    @Test
    fun testNumberParty(){
        val result = mockedViewModel.numberParty()
        assertEquals(0, result)
    }

    @Test
    fun testAdapterTime(){
        mockedViewModel.gameSelected("échec")
        val result = mockedViewModel.getAdapterTime()
        assertEquals(3, result.itemCount)
    }

    @Test
    fun testAdapterPlayer(){
        mockedViewModel.gameSelected("échec")
        val result = mockedViewModel.getAdapterPlayer()
        assertEquals(4, result.itemCount)
    }
}