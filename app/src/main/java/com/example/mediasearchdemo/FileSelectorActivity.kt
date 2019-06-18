package com.example.mediasearchdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.mediasearchdemo.bean.FileBean
import com.example.mediasearchdemo.bean.FileFolderBean
import com.example.mediasearchdemo.fileSelector.CursorSearchUtils
import com.example.mediasearchdemo.fileSelector.LoadManagerUtils
import com.jhj.slimadapter.SlimAdapter
import com.jhj.slimadapter.callback.ItemViewBind
import com.jhj.slimadapter.holder.ViewInjector
import com.jhj.slimadapter.itemdecoration.LineItemDecoration
import kotlinx.android.synthetic.main.activity_recyclerview.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class FileSelectorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)

        val type = intent.getIntExtra("type", 0)

        if (type == 0) {
            LoadManagerUtils.loadMedia(this, arrayOf(LoadManagerUtils.WORD)) {
                file(it)
            }
        } else {
            CursorSearchUtils.search(this) {
                file(it)
            }
        }


    }

    private fun file(it: List<FileFolderBean>) {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(LineItemDecoration())
        SlimAdapter.creator()
                .register(R.layout.list_item_file, object : ItemViewBind<FileBean> {
                    override fun convert(adapter: SlimAdapter, injector: ViewInjector, bean: FileBean, position: Int) {
                        injector.text(R.id.tv_file_name, bean.name)
                                .text(R.id.tv_file_update_time, sdf.format(bean.createTime))
                                .text(R.id.tv_file_size, formatFileSize(bean.size))
                                .text(R.id.tv_file_path, bean.path)
                    }
                })
                .attachTo(recyclerView)
                .setDataList(it[0].children)
    }

    fun formatFileSize(fileS: Long): String {
        val df = DecimalFormat("#.00")
        var fileSizeString = ""
        val wrongSize = "0B"
        if (fileS == 0L) {
            return wrongSize
        }
        if (fileS < 1024) {
            fileSizeString = df.format(fileS.toDouble()) + "B"
        } else if (fileS < 1048576) {
            fileSizeString = df.format(fileS.toDouble() / 1024) + "KB"
        } else if (fileS < 1073741824) {
            fileSizeString = df.format(fileS.toDouble() / 1048576) + "MB"
        } else {
            fileSizeString = df.format(fileS.toDouble() / 1073741824) + "GB"
        }
        return fileSizeString
    }

}