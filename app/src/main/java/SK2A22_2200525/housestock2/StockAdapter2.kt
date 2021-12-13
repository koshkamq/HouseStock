package SK2A22_2200525.housestock2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import org.w3c.dom.Text

class StockAdapter2 (data: OrderedRealmCollection<Stock>) :

        RealmRecyclerViewAdapter<Stock, StockAdapter2.ViewHolder>(data, true){

    //コールバックを実装する
    private var listener: ((Long?) -> Unit)? = null

    fun setOnItemClickListener(listener: (Long?) -> Unit){
        this.listener = listener
    }

    //変更した部分だけ更新するようにして高速化する
    init{
        setHasStableIds(true)
    }

    //商品名だけ一覧に表示している
    class ViewHolder(cell: View) : RecyclerView.ViewHolder(cell){
        val pname: TextView = cell.findViewById(android.R.id.text1)
        val money: TextView = cell.findViewById(android.R.id.text2)
    }

    //表示のデザインなどはここでviewtypeをイジって変える（今使ってるのはデザインが1種類しかないから使えない）
    //元々Android SDKに入ってるsimple_list_item_1っていうレイアウトでRecyclerViewに表示させてる（多分テキスト一行だけの構造）
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockAdapter2.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockAdapter2.ViewHolder, position: Int) {

        val stock: Stock? = getItem(position)
        holder.pname.text = stock?.pname
        holder.money.text = stock?.money.toString() + "円"
        holder.itemView.setOnClickListener{
            listener?.invoke(stock?.id)
        }
    }

    //idを返して高速化処理
    override fun getItemId(position: Int) : Long {
        return getItem(position)?.id ?: 0
    }
}