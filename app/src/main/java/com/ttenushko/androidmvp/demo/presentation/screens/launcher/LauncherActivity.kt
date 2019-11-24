package com.ttenushko.androidmvp.demo.presentation.screens.launcher

import android.os.Bundle
import com.ttenushko.androidmvp.demo.presentation.base.activity.BaseActivity
import com.ttenushko.androidmvp.demo.presentation.screens.home.HomeActivity

class LauncherActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HomeActivity.launch(this)
    }
}