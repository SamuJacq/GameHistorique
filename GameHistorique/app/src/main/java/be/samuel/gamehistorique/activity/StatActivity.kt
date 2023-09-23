package be.samuel.gamehistorique.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import be.samuel.gamehistorique.adapter.*
import be.samuel.gamehistorique.databinding.ActivityStatBinding
import be.samuel.gamehistorique.repository.PartyRepository
import be.samuel.gamehistorique.viewModel.StatViewModel
import java.util.*

class StatActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStatBinding
    private lateinit var statViewModel : StatViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        statViewModel = StatViewModel(PartyRepository.get())
        super.onCreate(savedInstanceState)
        binding = ActivityStatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rechercheButton.setOnClickListener {
            searchGame(binding.inputGameStat.text.toString())
        }

        binding.backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun searchGame(gameName : String) {
        if(statViewModel.gameSelected(gameName)){
            binding.numberPartyGame.text = "nombre de parties : ${statViewModel.numberParty()}"

            binding.recyclerViewAverageTime.adapter = statViewModel.getAdapterTime()
            binding.recyclerViewAverageTime.setHasFixedSize(true)

            binding.recyclerViewPlayer.adapter = statViewModel.getAdapterPlayer()
            binding.recyclerViewPlayer.setHasFixedSize(true)

            binding.errorMessageStat.text = ""
        }else{
            binding.errorMessageStat.text = "le jeu n'existe pas"
        }

    }


    companion object{
        fun newInstent(packageContext: Context): Intent {
            return Intent(packageContext, StatActivity::class.java).apply{

            }
        }
    }

}