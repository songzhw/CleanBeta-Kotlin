package cn.six.payx.ui

import android.os.Bundle
import cn.six.payx.R
import cn.six.payx.core.BaseActivity
import cn.six.payx.util.jump
import cn.six.payx.util.showToast
import cn.six.payx.view.LockPatternUtils
import cn.six.payx.view.LockPatternView

import kotlinx.android.synthetic.activity_unlock_pattern.*

// LockActivity has already store the lock pattern code.
//   :  LockPatternUtils.saveLockPattern( list<LockPatternView.Cell> );


public class UnlockActivity : BaseActivity(), LockPatternView.OnPatternListener {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unlock_pattern)

        lpvUnlock.isTactileFeedbackEnabled = true
        lpvUnlock.setOnPatternListener(this)

        //TODO (delete)    :   debug -- mock the saving process
        var lockUtil = LockPatternUtils(this)
        if( ! lockUtil.savedPatternExists() ){
            println("szw Unlock : has no lock pattern saved !")
            var list = arrayListOf(
                    LockPatternView.Cell(0,0),
                    LockPatternView.Cell(1,0),
                    LockPatternView.Cell(1,1),
                    LockPatternView.Cell(1,2)
            )
            LockPatternUtils(this).saveLockPattern(list)
        }

        //TODO (delete)    :   debug -- mock the saving process

    }

    override fun onPatternCellAdded(pattern: MutableList<LockPatternView.Cell>?) {
    }

    override fun onPatternStart() {
    }

    override fun onPatternCleared() {
    }

    override fun onPatternDetected(pattern: MutableList<LockPatternView.Cell>?) {
        var isMatched = LockPatternUtils(this).checkPattern(pattern)
        if(isMatched){
            jump(HomeActivity::class.java)
            this.finish()
        } else {
            showToast("lock pattern error !!!")
        }
    }
}