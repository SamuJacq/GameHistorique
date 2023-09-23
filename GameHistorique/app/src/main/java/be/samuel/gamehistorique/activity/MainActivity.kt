package be.samuel.gamehistorique.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import be.samuel.gamehistorique.adapter.ISwitchMain
import be.samuel.gamehistorique.databinding.ActivityCreatePartyBinding
import be.samuel.gamehistorique.databinding.ActivityMainBinding
import be.samuel.gamehistorique.viewModel.MainViewModel
import kotlinx.coroutines.runBlocking
import java.util.*

class MainActivity : AppCompatActivity(), ISwitchMain {

    private lateinit var binding : ActivityMainBinding
    private val mainViewModel : MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        runBlocking {
            mainViewModel.init()
        }
        super.onCreate(savedInstanceState)
        checkPermissions(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
    private fun checkPermissions(context: Context){
        val permissionsToRequest = ArrayList<String>()

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.CAMERA)
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(context as Activity, permissionsToRequest.toTypedArray(), 100)
        }
    }

    override fun switchDetail(idParty : UUID) {
        startActivity(DetailsPartyActivity.newInstent(this@MainActivity,mainViewModel.getCurrentId(idParty)))
    }

    override fun switchAddPlayer(idParty : UUID){
        startActivity(AddPlayerActivity.newInstent(this@MainActivity,mainViewModel.getCurrentId(idParty)))
    }

    override fun switchStat() {
        startActivity(StatActivity.newInstent(this@MainActivity))
    }

    override fun switchEdition() {
        startActivity(CreatePartyActivity.newInstent(this@MainActivity))
    }
    companion object{
        fun newInstent(packageContext: Context): Intent {
            return Intent(packageContext, MainActivity::class.java).apply{

            }
        }
    }
}