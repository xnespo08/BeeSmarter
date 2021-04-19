package com.example.mvvmlibrary


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

abstract class BaseMVVMActivity<out VM : ViewModel>(private val viewModelClass: Class<VM>) :
    BaseActivity() {

    protected open val viewModel: VM by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this).get(viewModelClass)
    }
}