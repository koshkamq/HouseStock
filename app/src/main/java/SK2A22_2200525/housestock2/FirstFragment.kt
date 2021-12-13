package SK2A22_2200525.housestock2

import SK2A22_2200525.housestock2.databinding.FragmentFirstBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.kotlin.where

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private lateinit var realm: Realm

    //データベースを使用するための準備
    //デフォルト設定のRealmインスタンスを取得する
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //収支ボタン
        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        //layoutmanager
        binding.list.layoutManager = LinearLayoutManager(context)

        //realmインスタンスからデータを取得するクエリ
        //whereメソッドの型引数でモデルの型を指定します
        //findallで全て取得し、変数に格納
        val Stocks = realm.where<Stock>().findAll().sort("term")

        //StockAdapterクラスのインスタンスを生成してRecyclerViewに設定
        val adapter = StockAdapter(Stocks)
        binding.list.adapter = adapter

        //コールバックを実装する
        adapter.setOnItemClickListener { id ->
            id?.let {
                val action = FirstFragmentDirections.actionToStockEditFragment(it)
                findNavController().navigate(action)
            }
        }

        (activity as? MainActivity)?.setFabVisible(View.VISIBLE)
        (activity as? MainActivity)?.setFab2Visible(View.VISIBLE)

        binding.Allbutton.setOnClickListener {
            val stocks = realm.where<Stock>().findAll().sort("term")
            val adapter = StockAdapter(stocks)

            binding.list.adapter = adapter

            adapter.setOnItemClickListener { id ->
                id?.let {
                    val action = FirstFragmentDirections.actionToStockEditFragment(it)
                    findNavController().navigate(action)
                }
            }

            binding.first.setBackgroundResource(R.drawable.backhouse4)
        }


        //食品ボタンを押すイベント
        binding.foodbutton.setOnClickListener {
            val stocks = realm.where<Stock>().equalTo("category", "食品").findAll().sort("term")
            val adapter = StockAdapter(stocks)

            binding.list.adapter = adapter

            adapter.setOnItemClickListener { id ->
                id?.let {
                    val action = FirstFragmentDirections.actionToStockEditFragment(it)
                    findNavController().navigate(action)
                }
            }

            binding.first.setBackgroundResource(R.drawable.niku)
        }

        //飲み物を押すイベント
        binding.drinkbutton.setOnClickListener {
            val stocks = realm.where<Stock>().equalTo("category", "飲み物").findAll().sort("term")
            val adapter = StockAdapter(stocks)

            binding.list.adapter = adapter

            adapter.setOnItemClickListener { id ->
                id?.let {
                    val action = FirstFragmentDirections.actionToStockEditFragment(it)
                    findNavController().navigate(action)
                }
            }

            binding.first.setBackgroundResource(R.drawable.drink)
        }

        //食品ボタンを押すイベント
        binding.nitiyouhinbutton.setOnClickListener {
            val stocks = realm.where<Stock>().equalTo("category", "日用品").findAll().sort("term")
            val adapter = StockAdapter(stocks)

            binding.list.adapter = adapter

            adapter.setOnItemClickListener { id ->
                id?.let {
                    val action = FirstFragmentDirections.actionToStockEditFragment(it)
                    findNavController().navigate(action)
                }
            }

            binding.first.setBackgroundResource(R.drawable.nitiyouhin)
        }
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy(){
        super.onDestroy()

        //アクティビティが終わると、Realmのインスタンスを閉じる
        realm.close()
    }


}