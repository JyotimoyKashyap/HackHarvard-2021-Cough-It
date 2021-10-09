package com.hackhack.coughit.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hackhack.coughit.repository.Repository

class ViewModelProviderFactory(val repository: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RestViewModel(repository = repository) as T
    }
}