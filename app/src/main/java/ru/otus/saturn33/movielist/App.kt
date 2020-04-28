package ru.otus.saturn33.movielist

import android.app.Application
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import ru.otus.saturn33.movielist.di.component.AppComponent
import ru.otus.saturn33.movielist.di.component.DaggerAppComponent
import ru.otus.saturn33.movielist.di.module.AppModule
import ru.otus.saturn33.movielist.di.module.DBModule
import ru.otus.saturn33.movielist.di.module.NetModule
import ru.otus.saturn33.movielist.service.FirebaseMessageService

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        instance = this

        initDI()
        initFCM()
    }

    private fun initDI() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .netModule(NetModule())
            .dBModule(DBModule(this))
            .build()
        appComponent.inject(this)
    }

    private fun initFCM() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            Log.d(FirebaseMessageService.TAG, "Current token: ${it.token}")
        }
    }

    companion object {
        var instance: App? = null
            private set
    }

}