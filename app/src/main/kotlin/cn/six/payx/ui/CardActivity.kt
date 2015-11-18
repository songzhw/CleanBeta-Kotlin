package cn.six.payx.ui

import kotlinx.android.synthetic.activity_cards.*
import android.os.Bundle
import cn.six.payx.R
import cn.six.payx.core.BaseActivity

public class CardActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cards)

        var cards = arrayListOf("Citi Bank","Standard Chartered Bank", "Royal Bank", "China Bank")
        var adapter = CardAdapter(this)
        adapter.data = cards
        lvCards.adapter = adapter

    }

}