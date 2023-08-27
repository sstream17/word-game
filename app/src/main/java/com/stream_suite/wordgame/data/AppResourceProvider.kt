package com.stream_suite.wordgame.data

import android.content.Context
import android.content.res.Resources
import com.stream_suite.wordgame.R

object AppResourceProvider {
    private lateinit var resources: Resources

    fun initialize(context: Context) {
        resources = context.resources
    }

    fun getDictionary(): String {
        val inputStream = resources.openRawResource(R.raw.dictionary)
        return inputStream.bufferedReader().use { it.readText() }
    }

    fun getPossibleAnswers(): String {
        val inputStream = resources.openRawResource(R.raw.possible_answers)
        return inputStream.bufferedReader().use { it.readText() }
    }
}