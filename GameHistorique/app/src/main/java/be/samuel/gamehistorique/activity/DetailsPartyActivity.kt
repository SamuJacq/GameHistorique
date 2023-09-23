package be.samuel.gamehistorique.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import be.samuel.gamehistorique.databinding.ActivityDetailsPartyBinding
import be.samuel.gamehistorique.repository.PartyRepository
import be.samuel.gamehistorique.viewModel.DetailsPartyViewModel
import be.samuel.gamehistorique.viewModel.PartyView
import java.io.FileNotFoundException
import java.util.*

private var EXTRA_ID_PARTY = UUID.fromString("42324b61-4fc1-4b56-9631-a902364011ef").toString()

class DetailsPartyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsPartyBinding
    private lateinit var detailsPartyViewModel : DetailsPartyViewModel //by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        detailsPartyViewModel = DetailsPartyViewModel(PartyRepository.get())
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsPartyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setView(detailsPartyViewModel.initParty(UUID.fromString(EXTRA_ID_PARTY)))
    }

    @SuppressLint("SetTextI18n")
    private fun setView(party : PartyView){
        binding.nameParty.text = "nom de la partie : ${party.name}"
        binding.descriptionParty.text = party.description
        binding.timeParty.text = "durée de la partie : ${party.time}"
        binding.numberPlayerParty.text = "nombre de joueur : ${party.numberPlayer}"
        binding.nameGame.text = "nom du jeu : ${party.nameGame}"
        binding.longitudeParty.text = "longitude : ${party.longitude}"
        binding.latitudeParty.text = "latitude : ${party.latitude}"
        setImage(party.photo, party.photoGame)
        binding.buttonBackButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.recyclerViewPlayer.adapter = detailsPartyViewModel.getAdapter()
    }

    private fun setImage(photoParty : String, photoGame : String){
        try{
            val bitmapParty = BitmapFactory.decodeFile(photoParty)
            if(bitmapParty != null) {
                binding.pictureParty.setImageBitmap(bitmapParty)
            }
        }catch(e : FileNotFoundException){

        }

        try{
            val bitmapGame = BitmapFactory.decodeFile(photoGame)
            if(bitmapGame != null){
                binding.pictureGame.setImageBitmap(bitmapGame)
            }
        }catch(e : FileNotFoundException){

        }
    }

    companion object{
        fun newInstent(packageContext: Context, idParty: UUID): Intent {
            return Intent(packageContext, DetailsPartyActivity::class.java).apply{
                EXTRA_ID_PARTY = idParty.toString()
            }
        }
    }

}
