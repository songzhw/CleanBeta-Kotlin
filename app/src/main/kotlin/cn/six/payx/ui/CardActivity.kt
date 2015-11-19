package cn.six.payx.ui

import kotlinx.android.synthetic.activity_cards.*
import android.os.Bundle
import android.view.View
import cn.six.payx.R
import cn.six.payx.core.BaseActivity
import cn.six.payx.presenter.CardPresenter
import kotlin.properties.Delegates

// show a list of bank
// clike one to delete this bank
// click the top TextView to revert the last deletion.

public class CardActivity : BaseActivity(){
    var adapter : CardAdapter by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cards)

        var presenter = CardPresenter(this)
        presenter.initData()

        adapter = CardAdapter(this)
        adapter.data = emptyList<String>()
        lvCards.adapter = adapter

        lvCards.setOnItemClickListener { adapterView, view, pos, lpos ->
            presenter.removeItem(pos)
        }


        tvCardsTop.visibility = View.GONE
        tvCardsTop.setOnClickListener{ view ->
            presenter.revertDelete()
        }
    }

    // refresh the whole list view
    fun refreshList(data : List<String>){
        adapter.data = data
        adapter.notifyDataSetChanged()
    }

    // revert the last deletion, refresh view
    fun refreshAfterRevert(data : List<String>){
        tvCardsTop.visibility = View.GONE
        refreshList(data)
    }

    // delete one Item, refresh view
    fun refreshAfterDeleteItem(deletedItem : String , data : List<String>){
        tvCardsTop.visibility = View.VISIBLE
        tvCardsTop.text = "You have deleted ${deletedItem}.  Click here to revert the deletion!"
        refreshList(data)
    }


}