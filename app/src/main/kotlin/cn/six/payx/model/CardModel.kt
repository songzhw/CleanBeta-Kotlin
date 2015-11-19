package cn.six.payx.model

public class CardModel {

    var bankCards = arrayListOf("Citi Bank",
            "Standard Chartered Bank",
            "Royal Bank",
            "China Bank")

    fun deleteItem(badLuck : String){
        bankCards.remove(badLuck)
    }

    fun addItem(item : String){
        bankCards.add(item)
    }

}