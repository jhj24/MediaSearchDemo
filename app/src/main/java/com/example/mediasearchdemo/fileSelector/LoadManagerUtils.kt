package com.example.mediasearchdemo.fileSelector

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import com.example.mediasearchdemo.bean.FileBean
import com.example.mediasearchdemo.bean.FileFolderBean
import java.io.File
import java.util.*

object LoadManagerUtils {

    /**
     * 纯文本
     */
    const val TEXT_PLAIN = "text/plain";// （纯文本）
    /**
     * （HTML文档）
     */
    const val TEXT_HTML = "text/html";
    /**
     * （XHTML文档）
     */
    const val XHTML = "application/xhtml+xml";

    /**
     * （GIF图像）
     */
    const val GIF = "image/gif";
    /**
     * 【PHP中为：image/pjpeg】 （JPEG图像）
     */
    const val JPEG = "mage/jpeg";
    /**
     * （PNG图像）【PHP中为：image/x-png】
     */
    const val PNG = "image/png";
    /**
     * （MPEG动画）
     */
    const val MPEG = "video/mpeg";
    /**
     * （任意的二进制数据）
     */
    const val OCTET = "application/octet-stream";
    /**
     * （PDF文档）
     */
    const val PDF = "application/pdf";
    /**
     * （Microsoft Word文件）
     */
    const val WORD = "application/msword";
    val WORDS = listOf(
            "application/msword",
            "application/doc",
            "appl/text",
            "application/vnd.msword",
            "application/vnd.ms-word",
            "application/winword",
            "application/word",
            "application/x-msw6",
            "application/x-msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

    /**
     * （RFC 822形式）
     */
    const val RFC = "message/rfc822";
    /**
     * （HTML邮件的HTML形式和纯文本形式，相同内容使用不同形式表示）
     */
    const val ALT = "multipart/alternative";
    /**
     * （使用HTTP的POST方法提交的表单）
     */
    const val FORM = "application/x-www-form-urlencoded";
    /**
     * （同上，但主要用于表单提交时伴随文件上传的场合）
     */
    const val FORM_DATA = "multipart/form-data";


    private val QUERY_URI = MediaStore.Files.getContentUri("external")
    private val PROJECTION = arrayOf(
            MediaStore.Files.FileColumns.TITLE, //文件名
            MediaStore.Files.FileColumns.DATA,  //路径
            MediaStore.Files.FileColumns.DATE_ADDED,//创建时间
            MediaStore.Files.FileColumns.MIME_TYPE, //mimeType
            MediaStore.Files.FileColumns.SIZE) //size
    private const val SELECTION = MediaStore.Files.FileColumns.MIME_TYPE + "=?"

    // asc 按升序排列 ,desc 按降序排列
    private const val ORDER_BY = MediaStore.Files.FileColumns._ID + " DESC"
    private const val type = 1

    fun loadMedia(activity: AppCompatActivity, mediaType: Array<String>, body: (List<FileFolderBean>) -> Unit) {
        var selection = SELECTION
        if (mediaType.size > 1) {
            mediaType.forEachIndexed { index, s ->
                if (index > 0) {
                    selection = "$selection OR $SELECTION"
                }
            }
        }

        LoaderManager.getInstance(activity).initLoader(type, null, object : LoaderManager.LoaderCallbacks<Cursor> {
            override fun onCreateLoader(p0: Int, p1: Bundle?): Loader<Cursor> {
                return CursorLoader(
                        activity,  //上下文
                        QUERY_URI, //要访问数据库的 uri地址
                        PROJECTION, //对应于数据库语句里的某列，如果只需要访问某几列，则传入这几列的名字即可，如果不传， 则默认访问全部数据
                        selection, // 筛选条件
                        mediaType, // 传入具体的参数，会替换上述 selection中的？
                        ORDER_BY   // 排序规则， 可以为空
                )
            }

            override fun onLoadFinished(p0: Loader<Cursor>, cursor: Cursor?) {
                try {
                    val folderBeanList = ArrayList<FileFolderBean>()
                    val mediaBeanList = ArrayList<FileBean>()
                    cursor?.let {
                        if (it.count > 0) {
                            it.moveToFirst()
                            do {
                                val title = it.getString(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE))
                                val data = it.getString(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))
                                val parentPath = it.getLong(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED))
                                val type = it.getString(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE))
                                val size = it.getLong(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE))
                                val bean = FileBean(title, data, parentPath, type, size)
                                mediaFolder(bean, folderBeanList)
                                mediaBeanList.add(bean)
                            } while (it.moveToNext())
                            if (mediaBeanList.size > 0) {
                                val bean = FileFolderBean()
                                bean.children = mediaBeanList
                                bean.name = "文档"
                                folderBeanList.add(0, bean)
                            }
                        }
                    }
                    body(folderBeanList)
                    LoaderManager.getInstance(activity).destroyLoader(type)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onLoaderReset(p0: Loader<Cursor>) {

            }
        })
    }

    private fun mediaFolder(bean: FileBean, folderBeanList: MutableList<FileFolderBean>) {
        if (bean.path != null) {
            val file = File(bean.path)
            val folderFile = file.parentFile
            for (folderBean in folderBeanList) {
                if (folderBean.name == folderFile.name) {
                    folderBean.children.add(bean)
                    return
                }
            }
            val folderBean = FileFolderBean()
            folderBean.name = folderFile.name
            folderBean.path = folderFile.path
            folderBean.children.add(bean)
            folderBeanList.add(folderBean)
        }

    }

}