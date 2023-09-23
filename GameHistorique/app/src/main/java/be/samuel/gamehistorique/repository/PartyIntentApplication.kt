package be.samuel.gamehistorique.repository

import android.app.Application

class PartyIntentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PartyRepository.initialize(this);
    }
}