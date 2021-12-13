package SK2A22_2200525.housestock2

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

//RealmObjectを継承してRealmのモデルを作る
//データベースでいう列を作る
open class Stock : RealmObject() {

    //idという変数を主キーにする（連番で登録される）
    @PrimaryKey
    var id: Long = 0

    //商品名
    var pname: String = ""
    //残量（小数可）
    var level: Double = 0.0
    //賞味期限（日付で格納する）（初期値は現在の日付）
    var term: Date = Date()
    //購入日時（家計簿の機能つけるなら必要かと思って一応）
    var date: Date = Date()
    //食品とか飲み物で分けるためのカテゴリ（Spinnerでできるか試し中）
    var category: String = ""
    //購入金額（家計簿用）
    var money: Int = 0

    //色（後々追加）
    var color: String = ""
}