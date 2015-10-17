package cn.six.payx.core

import android.app.Application
import android.content.Context
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.download.BaseImageDownloader
import kotlin.properties.Delegates



class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this

        // init Universal-Image-Loader
        val displayOptions = DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build()

        val imgConfig = ImageLoaderConfiguration.Builder(appContext)
                .denyCacheImageMultipleSizesInMemory()// 解码图像的大尺寸将在内存中缓存先前解码图像的小尺寸。
                .memoryCacheExtraOptions(512, 512)// 内存里的Bitmap最多就占1M。大了会降级
                .memoryCacheSizePercentage(15)// 只占app memory的15%。 默认值是1/8
                .defaultDisplayImageOptions(displayOptions)// 默认是不存硬盘的。这里要设置
                .imageDownloader(BaseImageDownloader(appContext, 5000, 30000))// connectTimeout为5秒，readTimeout为30秒
                .threadPoolSize(3).writeDebugLogs().build()
        ImageLoader.getInstance().init(imgConfig)

    }

    companion object {
        var appContext: Context by Delegates.notNull()
    }
}