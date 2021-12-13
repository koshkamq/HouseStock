package SK2A22_2200525.housestock2

import SK2A22_2200525.housestock2.databinding.FragmentNikuzyagaBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import io.realm.Realm
import io.realm.kotlin.where

class nikuzyagaFragment : Fragment() {

    private var _binding: FragmentNikuzyagaBinding? = null
    private val binding get() = _binding!!

    private lateinit var realm: Realm

    var husoku: String = ""

    //材料の配列
    val nikuzyagamaterial = listOf("豚肉", "じゃがいも", "人参", "玉ねぎ", "しょうゆ", "酒", "砂糖", "みりん", "顆粒出汁")
    val nikuzyagaamount = listOf(100, 1.5, 0.5, 0.5, 0, 0, 0, 0, 0)
    val nikuzyagatani = listOf("g", "個", "個", "個", "", "", "", "", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        realm = Realm.getDefaultInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = "肉じゃがの作り方"

        _binding = FragmentNikuzyagaBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //floatボタン非表示
        (activity as? MainActivity)?.setFabVisible(View.INVISIBLE)
        (activity as? MainActivity)?.setFab2Visible(View.INVISIBLE)


        binding.tohomebutton2.setOnClickListener {
            findNavController().navigate(R.id.action_nikuzyagaFragment_to_FirstFragment)
        }

        binding.completebutton2.setOnClickListener {

            //足りない食材を表示する
            for (i in 0..8) {
                val stock: Stock? = realm.where<Stock>().equalTo("pname", nikuzyagamaterial[i]).findFirst()
                //Log.d("sample",stock.toString())
                if (stock !== null) {

                    //量が足りないなら
                    if ((stock.level - nikuzyagaamount[i].toString().toDouble()) < 0) {
                        //husoku = husoku.plus("・" + stock.pname + ((stock.level - kareraisuamount[i].toString().toInt()) * -1) + kareraisutani[i] + "\n")

                        //realmを更新する(量がマイナスになれば削除）
                        realm.executeTransaction {
                            stock.deleteFromRealm()
                        }
                    } else {

                        //realmを更新する（量が足りてれば使った分を減らす）
                        realm.executeTransaction {
                            stock.level = stock.level - nikuzyagaamount[i].toString().toDouble()
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

            findNavController().navigate(R.id.action_nikuzyagaFragment_to_FirstFragment)
        }

        //足りない食材を表示する
        for (i in 0..8) {
            val stock: Stock? = realm.where<Stock>().equalTo("pname", nikuzyagamaterial[i]).findFirst()
            //Log.d("sample",stock.toString())
            if (stock === null) {

                husoku = husoku.plus("・" + nikuzyagamaterial[i])
                if (nikuzyagaamount[i] != 0) {
                    husoku = husoku.plus(nikuzyagaamount[i].toString() + nikuzyagatani[i].toString() + "\n")
                } else {
                    husoku = husoku.plus("\n")
                }

            } else {

                //量が足りないなら
                if ((stock.level - nikuzyagaamount[i].toString().toDouble()) < 0) {
                    husoku = husoku.plus("・" + stock.pname + ((stock.level - nikuzyagaamount[i].toString().toDouble()) * -1) + nikuzyagatani[i] + "\n")

                }

            }
        }

        //足りない材料を表示する
        binding.husokutext2.setText(husoku)
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