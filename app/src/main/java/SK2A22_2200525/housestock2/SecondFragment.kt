package SK2A22_2200525.housestock2

import SK2A22_2200525.housestock2.databinding.FragmentSecondBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.kotlin.where
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private lateinit var realm: Realm

    var expenditure = 0

    var spinnerposition = 0


    var result: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        (activity as? MainActivity)?.setFabVisible(View.INVISIBLE)
        (activity as? MainActivity)?.setFab2Visible(View.INVISIBLE)

        //layoutmanager
        binding.list2.layoutManager = LinearLayoutManager(context)

        //realmインスタンスからデータを取得するクエリ
        //whereメソッドの型引数でモデルの型を指定します
        //findallで全て取得し、変数に格納
        val Stocks = realm.where<Stock>().findAll()

        //StockAdapterクラスのインスタンスを生成してRecyclerViewに設定
        val adapter = StockAdapter2(Stocks)
        binding.list2.adapter = adapter


        //var calendar = Calendar.getInstance()

        //今月をスピナーにセットする
        binding.datespinner.setSelection(8)


        //日付スピナー選択したら
        binding.datespinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                findStock(2021, position, 1)

                spinnerposition = position

                //支出を計算
                var selectDate = Calendar.getInstance().apply {
                    clear()
                    set(2021, position, 1)
                }

                val stocks = realm.where<Stock>().between("date", selectDate.time, selectDate.apply {
                    add(Calendar.MONTH, 1)
                    add(Calendar.DAY_OF_MONTH, -1)
                }.time).findAll().sort("date")

                expenditure = 0
                for(expend in stocks){
                    expenditure += expend?.money!!
                }

                binding.expenditures.setText("支出：" + expenditure.toString() + "円")


                //収入を反映する（初入力時は０）
                result = (activity as? MainActivity)?.readtext("income" + position)
                binding.incometext.setText(result)


                //収支を計算する
                binding.balance.setText((result.toString().toInt() - expenditure).toString() + "円")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        //計算ボタンリスナー
        binding.keisan.setOnClickListener {
            //入力した収入を内部ストレージに保存する
            (activity as? MainActivity)?.savetext("income" + spinnerposition, binding.incometext.text.toString())

            //収支を計算する
            binding.balance.setText((binding.incometext.text.toString().toInt() - expenditure).toString() + "円")
        }


        //食品ボタンリスナー
        binding.kakeibofood.setOnClickListener {


            var selectDate = Calendar.getInstance().apply {
                clear()
                set(2021, spinnerposition, 1)
            }

            val stocks = realm.where<Stock>().between("date", selectDate.time, selectDate.apply {
                add(Calendar.MONTH, 1)
                add(Calendar.DAY_OF_MONTH, -1)
            }.time).equalTo("category", "食品").findAll().sort("date")

            val adapter = StockAdapter2(stocks)
            binding.list2.adapter = adapter
        }


        //飲み物ボタンリスナー
        binding.kakeibodrink.setOnClickListener {

            var selectDate = Calendar.getInstance().apply {
                clear()
                set(2021, spinnerposition, 1)
            }

            val stocks = realm.where<Stock>().between("date", selectDate.time, selectDate.apply {
                add(Calendar.MONTH, 1)
                add(Calendar.DAY_OF_MONTH, -1)
            }.time).equalTo("category", "飲み物").findAll().sort("date")

            val adapter = StockAdapter2(stocks)
            binding.list2.adapter = adapter
        }


        //日用品ボタンリスナー
        binding.kakeibonitiyouhinn.setOnClickListener {

            var selectDate = Calendar.getInstance().apply {
                clear()
                set(2021, spinnerposition, 1)
            }

            val stocks = realm.where<Stock>().between("date", selectDate.time, selectDate.apply {
                add(Calendar.MONTH, 1)
                add(Calendar.DAY_OF_MONTH, -1)
            }.time).equalTo("category", "日用品").findAll().sort("date")

            val adapter = StockAdapter2(stocks)
            binding.list2.adapter = adapter
        }


    }


    private fun findStock(year: Int, month: Int, dayofMonth: Int){
        var selectDate = Calendar.getInstance().apply {
            clear()
            set(year, month, dayofMonth)
        }

        val stocks = realm.where<Stock>().between("date", selectDate.time, selectDate.apply {
            add(Calendar.MONTH, 1)
            add(Calendar.DAY_OF_MONTH, -1)
        }.time).findAll().sort("date")

        val adapter = StockAdapter2(stocks)
        binding.list2.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

}