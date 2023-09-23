package be.samuel.gamehistorique.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import be.samuel.gamehistorique.databinding.ActivityAddPlayerBinding
import be.samuel.gamehistorique.viewModel.AddPlayerViewModel
import java.util.*

private var EXTRA_ID_PARTY = "UUID.random()"
class AddPlayerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddPlayerBinding
    private val addPlayerViewModel : AddPlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBackButton.setOnClickListener{
            backEdit()
        }

        binding.saveButton.setOnClickListener{
            saveParty()
        }
    }

    private fun saveParty(){
        val namePlayer = binding.namePlayerInput.text.toString()
        val score = binding.scoreInput.text.toString()
        if(namePlayer != "" && score != ""){
            addPlayerViewModel.addPlayer(UUID.fromString(EXTRA_ID_PARTY), namePlayer, score.toInt())
            Toast.makeText(this, "joueur ajouté", Toast.LENGTH_LONG).show()
            backEdit()
        }else{
            binding.errorMessageParty.text = "veuillez remplir tous les champs"
        }
    }

    private fun backEdit(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    companion object{
        fun newInstent(packageContext: Context, idParty : UUID): Intent {
            return Intent(packageContext, AddPlayerActivity::class.java).apply{
                EXTRA_ID_PARTY = idParty.toString()
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}