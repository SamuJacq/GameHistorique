package be.samuel.gamehistorique.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import be.samuel.gamehistorique.model.Game
import be.samuel.gamehistorique.databinding.ActivityHistoriqueItemBinding
import be.samuel.gamehistorique.model.Party
import be.samuel.gamehistorique.model.Player
import java.util.*

class PartyHistoriqueFragment : Fragment() {

    private var _binding :ActivityHistoriqueItemBinding? = null
    private val binding
        get() = checkNotNull(_binding){
            "Cannot access binding bacause it is null. Is the view is Visible ?"
        }
    private lateinit var party: Party

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        party = Party(
            idParty = UUID.randomUUID(),
            name = "échec",
            description = "duel avec mon père",
            time = 18,
            longitude = 0.0,
            latitude = 0.0,
            photo = "drawable/chess_icone.png",
            idGame = UUID.randomUUID()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityHistoriqueItemBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            partyNameAccueil.text = "jeu : ${party.name}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}