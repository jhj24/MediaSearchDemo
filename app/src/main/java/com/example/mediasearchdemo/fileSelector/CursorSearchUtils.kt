package com.example.mediasearchdemo.fileSelector

import android.annotation.SuppressLint
import android.provider.MediaStore
import android.support.v4.app.FragmentActivity
import com.example.mediasearchdemo.bean.FileBean
import com.example.mediasearchdemo.bean.FileFolderBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

object CursorSearchUtils {

    private val QUERY_URI = MediaStore.Files.getContentUri("external")
    private val PROJECTION = arrayOf(
            MediaStore.Files.FileColumns.TITLE, //文件名
            MediaStore.Files.FileColumns.DATA,  //路径
            MediaStore.Files.FileColumns.DATE_ADDED,//创建时间
            MediaStore.Files.FileColumns.MIME_TYPE, //mimeType
            MediaStore.Files.FileColumns.SIZE) //size
    private val SELECTOR = "(" + MediaStore.Files.FileColumns.DATA + " LIKE '%.doc'" + " or " + MediaStore.Files.FileColumns.DATA + " LIKE '%.docx'" + ")"


    @SuppressLint("CheckResult")
    fun search(activity: FragmentActivity, body: (List<FileFolderBean>) -> Unit) {
        //val dialog = activity.loadingDialog { HttpCall.cancelAll() }
        GlobalScope.launch(Dispatchers.IO) {
            val contentResolver = activity.contentResolver
            val cursor = contentResolver.query(QUERY_URI, PROJECTION, SELECTOR, null, null)
            val folderBeanList = ArrayList<FileFolderBean>()
            val mediaBeanList = ArrayList<FileBean>()
            cursor?.let {
                if (it.count > 0) {
                    it.moveToFirst()
                    do {
                        val title = it.getString(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE))//文件名，没后缀
                        val data = it.getString(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))
                        val time = it.getLong(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED))
                        val type = it.getString(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE))
                        val size = it.getLong(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE))
                        val file = File(data)
                        val bean = FileBean(file.name, data, time * 1000, type, size)
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
            cursor?.close()
            launch(Dispatchers.Main) {
                body(folderBeanList)
                //dialog.dismiss()
            }
        }
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