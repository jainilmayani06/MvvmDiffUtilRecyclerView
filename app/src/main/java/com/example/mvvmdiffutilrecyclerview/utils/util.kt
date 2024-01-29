package com.example.mvvmdiffutilrecyclerview.utils

import android.app.Dialog
import android.widget.LinearLayout

fun Dialog.setupDialog(layoutResId: Int){
    setContentView(layoutResId)
    window!!.setLayout(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    setCancelable(true)
}
