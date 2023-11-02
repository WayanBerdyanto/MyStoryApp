package com.dicoding.mystoryapp

import com.dicoding.mystoryapp.data.remote.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) { // Buat list story items
            val storyItems = ListStoryItem(
                i.toString(),
                "Photo $i",
                "Created $i",
                "name $i",
                "Description $i",
                i.toDouble(),
                i.toDouble(),
            )
            items.add(storyItems)
        }
        return items
    }
}