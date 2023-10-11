package com.example.menti.data

import com.example.menti.data.model.Category

class Categories {

    fun loadCategories(): List<Category> {
        return mutableListOf(
            Category("Angststörungen", false),
            Category("Beziehung", false),
            Category("Borderline", false),
            Category("Burnout", false),
            Category("Depressionen", false),
            Category("Familie", false),
            Category("Gewalt", false),
            Category("Paartherapie", false),
            Category("Sucht", false),
            Category("PTBS", false),
            Category("Trauma", false),
            Category("Panikattacken", false),
            Category("Suizidgedanken", false)
        )
    }
}