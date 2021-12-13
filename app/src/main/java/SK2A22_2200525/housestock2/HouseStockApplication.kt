package SK2A22_2200525.housestock2

import android.app.Application
import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration

//Applicationクラスを継承する
class HouseStockApplication : Application (){


    //ApplicationクラスのonCreateメソッドをオーバーライドする
    //アプリの実行時にアクティビティよりも先に呼ばれるので、ここでデータベースの設定をする
    override fun onCreate() {
        super.onCreate()

        //context = applicationContext

        //realmを初期化する
        Realm.init(this)

        //realmの初期設定
        val config = RealmConfiguration.Builder()
            .allowWritesOnUiThread(true).build()
        Realm.setDefaultConfiguration(config)



    }
}