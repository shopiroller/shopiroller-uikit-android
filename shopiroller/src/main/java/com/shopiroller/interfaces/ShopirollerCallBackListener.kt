package com.shopiroller.interfaces

interface ShopirollerCallBackListener<T> {
    fun onSuccess(data: T)
    fun onError(errorMessage: String)
}