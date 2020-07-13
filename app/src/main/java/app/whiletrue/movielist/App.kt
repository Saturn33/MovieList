package app.whiletrue.movielist

import android.app.Application
//import android.util.Log
//import com.google.firebase.iid.FirebaseInstanceId
import app.whiletrue.movielist.di.component.AppComponent
import app.whiletrue.movielist.di.component.DaggerAppComponent
import app.whiletrue.movielist.di.module.AppModule
import app.whiletrue.movielist.di.module.DBModule
import app.whiletrue.movielist.di.module.NetModule
//import app.whiletrue.movielist.service.FirebaseMessageService

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        instance = this

        initDI()
//        initFCM()
    }

    private fun initDI() {
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .netModule(NetModule())
            .dBModule(DBModule(this))
            .build()
    }

/*
    private fun initFCM() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            Log.d(FirebaseMessageService.TAG, "Current token: ${it.token}")
        }
    }
*/

    companion object {
        var instance: App? = null
            private set
    }

}