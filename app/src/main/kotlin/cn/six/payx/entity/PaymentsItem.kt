package cn.six.payx.entity

public interface IPaymentItem{
    var type : String
    var bank : String
    var number : String
        get() = "0.00"
        set(value) { number = value   }
}

public class AccountBalance : IPaymentItem{
    override var type : String = "Balance"
    override var bank : String = "Account"
}

public class CreditCard(val bankName : String) : IPaymentItem{
    override var type : String = "Credit Card"
    override var bank : String = bankName
}

public class DebitCard(val bankName : String) : IPaymentItem{
    override var type : String = "Debit Card"
    override var bank : String = bankName
}
