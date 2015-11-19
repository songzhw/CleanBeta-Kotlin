package cn.six.payx.presenter

import cn.six.payx.model.CardModel
import cn.six.payx.ui.CardActivity
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

/*
Note:
  A better way is to transfer ICardActivity, instead of CardActivity, to this Presenter
*/
public class CardPresenter(var view: CardActivity){
    var model : CardModel by Delegates.notNull()
    var cards : List<String> by Delegates.notNull()
    var cachedDeleted : String by Delegates.notNull()

    init{
        model = CardModel()
    }

    fun initData(){
        // mock the server connecting
        Observable.just("")
            .delay(2, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                cards = model.bankCards
                view.refreshList(cards)
            }
    }


    fun removeItem( pos : Int){
        cachedDeleted = cards.get(pos)
        model.deleteItem(cachedDeleted)
        cards = model.bankCards
        view.refreshAfterDeleteItem(cachedDeleted, cards)
    }

    fun revertDelete(){
        model.addItem(cachedDeleted)
        view.refreshAfterRevert(model.bankCards)
    }

}