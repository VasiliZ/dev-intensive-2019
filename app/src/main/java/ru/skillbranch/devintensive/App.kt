package ru.skillbranch.devintensive

import android.app.Application
import android.content.Context

class App:Application(){
    companion object{
        private var instacne:App? = null

        fun applicationContext():Context{
            return instacne!!.applicationContext
        }

    }
    init {
        instacne = this
    }
    override fun onCreate() {
        super.onCreate()
    }
}