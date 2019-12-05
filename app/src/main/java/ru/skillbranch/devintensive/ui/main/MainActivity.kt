package ru.skillbranch.devintensive.ui.main

import android.app.Activity
import android.os.Bundle
import ru.skillbranch.devintensive.R

class MainActivity:Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        initToolbar()
        initViews()
        initViewModel()
    }

    private fun initViewModel() {


    }

    private fun initViews() {


    }

    private fun initToolbar() {


    }
}