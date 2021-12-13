package SK2A22_2200525.housestock2

import SK2A22_2200525.housestock2.databinding.FragmentOmuraisuBinding
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import io.realm.Realm
import io.realm.kotlin.where

class omuraisuFragment : Fragment() {

    private var _binding: FragmentOmuraisuBinding? = null
    private val binding get() = _binding!!

    private lateinit var realm: Realm


    var husoku: String = ""


    //材料の配列
    val omuraisumaterial = listOf("白米", "鶏モモ肉", "玉ねぎ", "ケチャップ", "卵")
    val omuraisuamount = listOf(200, 50, 0.25, 0, 2)
    val omuraisutani = listOf("g", "g", "個", "", "個")

    var list = mutableListOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        realm = Realm.getDefaultInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = "オムライスの作り方"

        _binding = FragmentOmuraisuBinding.inflate(inflater, container, false)
        return binding.root
    }
    

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //floatボタン非表示
        (activity as? MainActivity)?.setFabVisible(View.INVISIBLE)
        (activity as? MainActivity)?.setFab2Visible(View.INVISIBLE)


        //ホームへ
        binding.tohomebutton.setOnClickListener {
            findNavController().navigate(R.id.action_omuraisuFragment_to_FirstFragment)
        }


        //作ったを押すと減らす
        binding.completebutton.setOnClickListener {

            //足りない食材を表示する
            for(i in 0..4) {
                val stock: Stock? = realm.where<Stock>().equalTo("pname", omuraisumaterial[i]).findFirst()
                //Log.d("sample",stock.toString())
                if (stock !== null) {


                    //量が足りないなら
                    if ((stock.level - omuraisuamount[i].toString().toDouble()) < 0) {
                        //husoku = husoku.plus("・" + stock.pname + ((stock.level - omuraisuamount[i].toString().toInt()) * -1) + omuraisutani[i] + "\n")

                        //realmを更新する(量がマイナスになれば削除）
                        realm.executeTransaction {
                            stock.deleteFromRealm()
                        }
                    } else {

                        //realmを更新する（量が足りてれば使った分を減らす）
                        realm.executeTransaction {
                            stock.level = stock.level - omuraisuamount[i].toString().toDouble()
                        }

                        //量が0になれば消す
                        if (stock.level == 0.0) {
                            realm.executeTransaction {
                                stock.deleteFromRealm()
                            }
                        }
                    }

                }
            }

            findNavController().navigate(R.id.action_omuraisuFragment_to_FirstFragment)
        }


        //足りない食材を表示する
        for(i in 0..4) {
            val stock: Stock? = realm.where<Stock>().equalTo("pname", omuraisumaterial[i]).findFirst()
            //Log.d("sample",stock.toString())
            if(stock === null){

                husoku = husoku.plus("・" + omuraisumaterial[i])
                if(omuraisuamount[i] != 0){
                    husoku = husoku.plus( omuraisuamount[i].toString() + omuraisutani[i].toString() + "\n")
                }else{
                    husoku = husoku.plus("\n")
                }

            }else{

                //量が足りないなら
                if((stock.level - omuraisuamount[i].toString().toDouble()) < 0) {
                    husoku = husoku.plus("・" + stock.pname + ((stock.level - omuraisuamount[i].toString().toDouble()) * -1) + omuraisutani[i] + "\n")

                }

            }
        }

        //足りない材料を表示する
        binding.husokutext.setText(husoku)

    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy(){
        super.onDestroy()
        realm.close()
    }
}