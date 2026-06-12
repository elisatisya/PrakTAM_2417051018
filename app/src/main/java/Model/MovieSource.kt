package com.example.praktam_2417051018.model

import android.content.Context

object MovieSource {

    fun getResourceId(context: Context, imageName: String): Int {
        return context.resources.getIdentifier(
            imageName,
            "drawable",
            context.packageName
        )
    }
}