package SK2A22_2200525.housestock2

import SK2A22_2200525.housestock2.databinding.ActivityMainBinding
import android.content.Context
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import io.realm.Realm
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    //private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))


        val naviController = findNavController(R.id.nav_host_fragment)
        //アクションバーに戻るボタンを追加する処理
        setupActionBarWithNavController(naviController)

        //画面遷移を行う
        binding.fab.setOnClickListener { view ->
            naviController.navigate(R.id.action_to_stockEditFragment)
        }

        //レシピ画面への遷移
        binding.fab2.setOnClickListener { view ->
            naviController.navigate(R.id.action_FirstFragment_to_recipeFragment)
        }
        

    }

    override fun onSupportNavigateUp()
            = findNavController(R.id.nav_host_fragment).navigateUp()

    //編集画面に行くと作成（+）ボタンが非表示になる
    //これは表示している
    fun setFabVisible(visibility: Int){
        binding.fab.visibility = visibility
    }

    //レシピボタンを表示にする
    fun setFab2Visible(visibility: Int){
        binding.fab2.visibility = visibility
    }

    //テキスト保存
    fun savetext(file: String, str: String){
        applicationContext.openFileOutput(file, Context.MODE_PRIVATE).use{
            it.write(str.toByteArray())
        }
    }

    //テキスト読み出し
    fun readtext(filename: String): String {
        try {
            val buf: BufferedReader = readFile(filename)
            val result = buf.use { it.readText() }
            return result
        }catch(e: FileNotFoundException){
            return "0"
        }
    }

    private fun readFile(file: String): BufferedReader {
        val readFile = File(applicationContext.filesDir, file)
        return readFile.bufferedReader()
    }


}

