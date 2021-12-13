package SK2A22_2200525.housestock2

import SK2A22_2200525.housestock2.databinding.FragmentStockEditBinding
import android.app.ProgressDialog.show
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class StockEditFragment : Fragment() {

    private var _binding: FragmentStockEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var realm: Realm

    var setcategory = 0
    var setcolor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        realm = Realm.getDefaultInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStockEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val args: StockEditFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //更新処理を実装
        if(args.stockId != -1L) {
            val stock = realm.where<Stock>().equalTo("id", args.stockId).findFirst()
            binding.pnameEdit.setText(stock?.pname)
            binding.levelEdit.setText(stock?.level.toString())
            binding.termEdit.setText(android.text.format.DateFormat.format("yyyy/MM/dd", stock?.term))
            binding.dateEdit.setText(android.text.format.DateFormat.format("yyyy/MM/dd", stock?.date))
            binding.moneyEdit.setText(stock?.money.toString())

            if(stock?.category.toString() == "食品"){
                setcategory = 0
            }else if(stock?.category.toString() == "飲み物"){
                setcategory = 1
            }else if(stock?.category.toString() == "日用品"){
                setcategory = 2
            }

            if(stock?.color.toString() == "なし"){
                setcolor = 0
            }else if(stock?.color.toString() == "青"){
                setcolor = 1
            }else if(stock?.color.toString() == "赤"){
                setcolor = 2
            }

            binding.category.setSelection(setcategory)
            binding.colorspinner.setSelection(setcolor)

            //表示処理
            binding.delete.visibility = View.VISIBLE
        }else{
            binding.delete.visibility = View.INVISIBLE
        }
        (activity as? MainActivity)?.setFabVisible(View.INVISIBLE)
        (activity as? MainActivity)?.setFab2Visible(View.INVISIBLE)
        binding.save.setOnClickListener { saveStock(it) }
        binding.delete.setOnClickListener { deleteStock(it) }


        //日付選択ダイアログ
        binding.termButton.setOnClickListener {
            DateDialog{ date ->
                binding.termEdit.setText(date)
            }.show(parentFragmentManager, "date_dialog")
        }

        binding.dateButton.setOnClickListener {
            DateDialog{ date ->
                binding.dateEdit.setText(date)
            }.show(parentFragmentManager, "date_dailog")
        }
    }

    private fun saveStock(view: View) {

        when (args.stockId) {
            -1L -> {
                realm.executeTransaction { db: Realm ->
                    val maxId = db.where<Stock>().max("id")
                    val nextId = (maxId?.toLong() ?: 0L) + 1L
                    val stock = db.createObject<Stock>(nextId)

                    stock.pname = binding.pnameEdit.text.toString()
                    try {
                        stock.level = binding.levelEdit.text.toString().toDouble()
                    }catch (e: NumberFormatException){
                        stock.level = 0.0
                    }
                    val date = (("${binding.termEdit.text}").toString()).toDate()
                    //Log.d("Smaple",date.toString())
                    if (date != null) stock.term = date
                    val date2 = (("${binding.dateEdit.text}").toString()).toDate()
                    if (date2 != null) stock.date = date2
                    //↓スピナーでカテゴリ追加
                    stock.category = binding.category.selectedItem.toString()
                    try {
                        stock.money = binding.moneyEdit.text.toString().toInt()
                    }catch (e: NumberFormatException){
                        stock.money = 0
                    }

                    //色追加
                    stock.color = binding.colorspinner.selectedItem.toString()
                }
                Snackbar.make(view, "追加しました", Snackbar.LENGTH_SHORT)
                    .setAction("戻る") { findNavController().popBackStack() }
                    .setActionTextColor(Color.YELLOW)
                    .show()
            }
            else -> {
                realm.executeTransaction { db: Realm ->
                    val stock = db.where<Stock>().equalTo("id", args.stockId).findFirst()

                    stock?.pname = binding.pnameEdit.text.toString()
                    stock?.level = binding.levelEdit.text.toString().toDouble()
                    val date = (("${binding.termEdit.text}").toString()).toDate()
                    if (date != null) stock?.term = date
                    val date2 = (("${binding.dateEdit.text}").toString()).toDate()
                    if (date2 != null) stock?.date = date2
                    //↓スピナーでカテゴリ追加
                    stock?.category = binding.category.selectedItem.toString()
                    stock?.money = binding.moneyEdit.text.toString().toInt()

                    //色
                    stock?.color = binding.colorspinner.selectedItem.toString()
                }
                Snackbar.make(view, "修正しました", Snackbar.LENGTH_SHORT)
                    .setAction("戻る") { findNavController().popBackStack() }
                    .setActionTextColor(Color.YELLOW)
                    .show()
            }
        }
    }

    private fun deleteStock(view: View){
        realm.executeTransaction { db: Realm ->
            db.where<Stock>().equalTo("id", args.stockId)?.findFirst()?.deleteFromRealm()
        }
        Snackbar.make(view, "削除しました", Snackbar.LENGTH_SHORT)
            .setActionTextColor(Color.YELLOW)
            .show()

        findNavController().popBackStack()
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy(){
        super.onDestroy()
        realm.close()
    }

    private fun String.toDate(pattern: String = "yyyy/MM/dd"): Date?{
        return try{
            SimpleDateFormat(pattern).parse(this)
        }catch (e: IllegalArgumentException){
            return null
        }catch (e: ParseException){
            return null
        }
    }

}
