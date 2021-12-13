package SK2A22_2200525.housestock2

import SK2A22_2200525.housestock2.databinding.FragmentHoikoroBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentHostCallback
import androidx.navigation.fragment.findNavController
import io.realm.Realm
import io.realm.kotlin.where

class hoikoroFragment : Fragment() {

    private var _binding: FragmentHoikoroBinding? = null
    private val binding get() = _binding!!

    private lateinit var realm: Realm

    var husoku: String = ""

    //材料の配列
    val hoikoromaterial = listOf("豚肉", "キャベツ", "長ネギ", "ピーマン", "醤油", "甜麺醤", "酒", "砂糖", "片栗粉")
    val hoikoroamount = listOf(75, 0.125, 0.25, 1, 0, 0, 0, 0, 0)
    val hoikorotani = listOf("g", "個", "個", "個", "", "", "", "", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        realm = Realm.getDefaultInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHoikoroBinding.inflate(inflater, container, false)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "回鍋肉の作り方"
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //floatボタン非表示
        (activity as? MainActivity)?.setFabVisible(View.INVISIBLE)
        (activity as? MainActivity)?.setFab2Visible(View.INVISIBLE)


        binding.tohomebutton4.setOnClickListener {
            findNavController().navigate(R.id.action_hoikoroFragment_to_FirstFragment)
        }

        binding.completebutton4.setOnClickListener {

            //足りない食材を表示する
            for (i in 0..8) {
                val stock: Stock? = realm.where<Stock>().equalTo("pname", hoikoromaterial[i]).findFirst()
                //Log.d("sample",stock.toString())
                if (stock !== null) {

                    //量が足りないなら
                    if ((stock.level - hoikoroamount[i].toString().toDouble()) < 0) {
                        //husoku = husoku.plus("・" + stock.pname + ((stock.level - kareraisuamount[i].toString().toInt()) * -1) + kareraisutani[i] + "\n")

                        //realmを更新する(量がマイナスになれば削除）
                        realm.executeTransaction {
                            stock.deleteFromRealm()
                        }
                    } else {

                        //realmを更新する（量が足りてれば使った分を減らす）
                        realm.executeTransaction {
                            stock.level = stock.level - hoikoroamount[i].toString().toDouble()
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

            findNavController().navigate(R.id.action_hoikoroFragment_to_FirstFragment)
        }

        //足りない食材を表示する
        for (i in 0..8) {
            val stock: Stock? = realm.where<Stock>().equalTo("pname", hoikoromaterial[i]).findFirst()
            //Log.d("sample",stock.toString())
            if (stock === null) {

                husoku = husoku.plus("・" + hoikoromaterial[i])
                if (hoikoroamount[i] != 0) {
                    husoku = husoku.plus(hoikoroamount[i].toString() + hoikorotani[i].toString() + "\n")
                } else {
                    husoku = husoku.plus("\n")
                }

            } else {

                //量が足りないなら
                if ((stock.level - hoikoroamount[i].toString().toDouble()) < 0) {
                    husoku = husoku.plus("・" + stock.pname + ((stock.level - hoikoroamount[i].toString().toDouble()) * -1) + hoikorotani[i] + "\n")

                }

            }
        }

        //足りない材料を表示する
        binding.husokutext4.setText(husoku)
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