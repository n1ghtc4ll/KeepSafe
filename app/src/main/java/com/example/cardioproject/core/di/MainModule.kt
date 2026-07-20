package com.example.cardioproject.core.di

import com.example.cardioproject.Main
import com.example.cardioproject.core.navigation.Route
import com.example.cardioproject.core.navigation.TopLevelBackStack
import org.koin.dsl.module

val mainModule = module {
    single { TopLevelBackStack<Route>(Main) }
}