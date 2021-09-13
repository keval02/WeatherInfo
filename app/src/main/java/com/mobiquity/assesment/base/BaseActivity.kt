package com.mobiquity.assesment.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResourceId())
    }

    /* override this method in activity to attach the layout*/
    abstract fun getLayoutResourceId(): Int
}