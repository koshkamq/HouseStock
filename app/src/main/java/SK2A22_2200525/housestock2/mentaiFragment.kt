package SK2A22_2200525.housestock2

import SK2A22_2200525.housestock2.databinding.FragmentMentaiBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import io.realm.Realm
import io.realm.kotlin.where

class mentaiFragment : Fragment() {

    private var _binding: FragmentMentaiBinding? = null
    private val binding get() = _binding!!

    private lateinit var realm: Realm

    var husoku: String = ""

    //材料の配列
    val mentaimaterial = listOf("明太子", "マヨネーズ", "めんつゆ", "バター", "スパゲティ", "刻み海苔")
    val mentaiamount = listOf(40, 0, 15, 0, 100, 0)
    val mentaitani = listOf("g", "", "g", "", "g", "")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        realm = Realm.getDefaultInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.title = "明太パスタの作り方"

        _binding = FragmentMentaiBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //floatボタン非表示
        (activity as? MainActivity)?.setFabVisible(View.INVISIBLE)
        (activity as? MainActivity)?.setFab2Visible(View.INVISIBLE)


        binding.tohomebutton3.setOnClickListener {
            findNavController().navigate(R.id.action_mentaiFragment_to_FirstFragment)
        }

        binding.completebutton3.setOnClickListener {

            //足りない食材を表示する
            for (i in 0..5) {
                val stock: Stock? = realm.where<Stock>().equalTo("pname", mentaimaterial[i]).findFirst()
                //Log.d("sample",stock.toString())
                if (stock !== null) {

                    //量が足りないなら
                    if ((stock.level - mentaiamount[i].toString().toDouble()) < 0) {
                        //husoku = husoku.plus("・" + stock.pname + ((stock.level - kareraisuamount[i].toString().toInt()) * -1) + kareraisutani[i] + "\n")

                        //realmを更新する(量がマイナスになれば削除）
                        realm.executeTransaction {
                            stock.deleteFromRealm()
                        }
                    } else {

                        //realmを更新する（量が足りてれば使った分を減らす）
                        realm.executeTransaction {
                            stock.level = stock.level - mentaiamount[i].toString().toDouble()
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

            findNavController().navigate(R.id.action_mentaiFragment_to_FirstFragment)
        }

        //足りない食材を表示する
        for (i in 0..5) {
            val stock: Stock? = realm.where<Stock>().equalTo("pname", mentaimaterial[i]).findFirst()
            //Log.d("sample",stock.toString())
            if (stock === null) {

                husoku = husoku.plus("・" + mentaimaterial[i])
                if (mentaiamount[i] != 0) {
                    husoku = husoku.plus(mentaiamount[i].toString() + mentaitani[i].toString() + "\n")
                } else {
                    husoku = husoku.plus("\n")
                }

            } else {

                //量が足りないなら
                if ((stock.level - mentaiamount[i].toString().toDouble()) < 0) {
                    husoku = husoku.plus("・" + stock.pname + ((stock.level - mentaiamount[i].toString().toDouble()) * -1) + mentaitani[i] + "\n")

                }

            }
        }

        //足りない材料を表示する
        binding.husokutext3.setText(husoku)
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