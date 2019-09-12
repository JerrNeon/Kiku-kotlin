package com.jn.kikukt.utils.glide

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule
import com.jn.kikukt.common.utils.getMemoryInfo

/**
 * Author：Stevie.Chen Time：2019/7/11
 * Class Comment：
 */
@GlideModule
class GlideModuleConfig : AppGlideModule(){

    private val diskSize = 1024 * 1024 * 100//100M
    private val memorySize = Runtime.getRuntime().maxMemory().toInt() / 8  // 取1/8最大内存作为最大缓存

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        // 定义缓存大小和位置
        //builder.setDiskCache(InternalCacheDiskCacheFactory(context, diskSize.toLong()))  //内存中
        //builder.setDiskCache(ExternalCacheDiskCacheFactory(context, "cache", diskSize)) //sd卡中

        // 默认内存和图片池大小
        //val calculator = MemorySizeCalculator.Builder(context).build()
        //val defaultMemoryCacheSize = calculator.memoryCacheSize // 默认内存大小
        //val defaultBitmapPoolSize = calculator.bitmapPoolSize // 默认图片池大小
        //builder.setMemoryCache(LruResourceCache(defaultMemoryCacheSize.toLong())) // 该两句无需设置，是默认的
        //builder.setBitmapPool(LruBitmapPool(defaultBitmapPoolSize.toLong()))

        // 自定义内存和图片池大小
        //builder.setMemoryCache(LruResourceCache(memorySize.toLong()))
        //builder.setBitmapPool(LruBitmapPool(memorySize.toLong()))

        // 定义图片格式
        //builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888)//设置ARGB_8888是为了防止小屏手机过渡压缩导致图片底色出现浅绿色的问题
        //builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565) // 默认
        val memoryInfo = context.getMemoryInfo()
        if (null != memoryInfo) {
            //builder.setDecodeFormat(memoryInfo.lowMemory ? DecodeFormat.PREFER_RGB_565 : DecodeFormat.PREFER_ARGB_8888);
        }
        //默认大小是250M,缓存文件放在APP的缓存文件夹下。
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, diskSize.toLong()))
        builder.setMemoryCache(LruResourceCache(memorySize.toLong()))
    }

}