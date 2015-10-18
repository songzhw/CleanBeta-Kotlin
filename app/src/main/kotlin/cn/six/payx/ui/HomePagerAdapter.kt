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
import kotlin.properties.Delegates

/**
 * Created by songzhw on 10/17/15.
 */

public class HomePagerAdapter(var ctx : Context) : PagerAdapter(){
    var imageLoader : ImageLoader = ImageLoader.getInstance()
    var inflater : LayoutInflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    var data : List<BannerItem> by Delegates.notNull()


    override fun getCount(): Int {
        return data.size();
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

        var view : ImageView = inflater.inflate(R.layout.view_single_image, null) as ImageView
        imageLoader.displayImage(item.bannerImgUrl, view)

        return view;
    }
}