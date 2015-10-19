package cn.six.payx.ui

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import cn.six.payx.R
import cn.six.payx.entity.BannerItem
import com.nostra13.universalimageloader.core.ImageLoader
import java.util.*
import kotlin.properties.Delegates

/**
 * Created by songzhw on 10/17/15.
 */

public class HomePagerAdapter(var ctx : Context) : PagerAdapter(){
    var imageLoader : ImageLoader = ImageLoader.getInstance()
    var inflater : LayoutInflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    var data : List<BannerItem> by Delegates.notNull()

    // to get a "endless" loop
    override fun getCount(): Int {
        var size = data.size()
        return if(size <= 0) 0 else 2000
    }

    override fun isViewFromObject(view: View?, aobj: Any?): Boolean {
        return view == aobj
    }

    override fun destroyItem(container: ViewGroup?, position: Int, aobj: Any?) {
        container?.removeView(aobj as View)
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any? {
        // the onCreate() has no data, so if we don't use this code
        // a error will show : java.lang.ArithmeticException: divide by zero
        if(data.size() <= 0 ){
            return null
        }

        var index = position % data.size()
        var item = data[index]

        var view = inflater.inflate(R.layout.view_single_image, null)
        var iv : ImageView = view.findViewById(R.id.iv_single_image) as ImageView
        imageLoader.displayImage(item.bannerImgUrl, iv)

        container?.addView(view)

        return view;
    }


    // to get the middle position of 2000("endless")
    fun getFirstItemIndex(wantedIndex: Int): Int {
        if (data.size() == 0) {
            return 1000
        } else {
            for (i in 1000 downTo 0) {
                if ((i % data.size()) == wantedIndex) {
                    return i
                }
            }
        }
        return 0
    }
}