package com.ismail.creatvt.indigowallet.utility

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <X, Y> mergeAll(vararg args: LiveData<X>, function:(List<X?>)->Y):LiveData<Y>{
    val result = MediatorLiveData<Y>()

    for(arg in args){
        result.addSource(arg){
            val array = args.map {
                it.value
            }
            result.postValue(function(array))
        }
    }
    return result
}