package com.dyotakt.hawaldar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.math.floor

class MainViewModel: ViewModel() {
//    val _myList: MutableLiveData<List<items>> = MutableLiveData<List<items>>()
    private val _myList = MutableLiveData<List<items>>(listOf(
        items("Steam",(floor(Math.random()*100000)).toString(),"Testing123","023026033", 123456),
        items("Google",(floor(Math.random()*100000)).toString(),"Testing1234","234234234", 123456),
        items("Steam",(floor(Math.random()*100000)).toString(),"Testing123","023026033", 123456),
        items("Google",(floor(Math.random()*100000)).toString(),"Testing1234","234234234", 123456),
        items("Steam",(floor(Math.random()*100000)).toString(),"Testing123","023026033", 123456),
        items("Google",(floor(Math.random()*100000)).toString(),"Testing1234","234234234", 123456),
        items("Facebook",(floor(Math.random()*100000)).toString(),"Testing12345","024119242", 123456)
    ))
    val myList: LiveData<List<items>> = _myList

    val t1: totp = totp()
    var timerValue: Int = 0


    init {
        fetchAndPopulateData()
        iterateAndGetOtp()
        collectFlow()
    }

    fun fetchAndPopulateData() {
        _myList.value = _myList.value?.plus(items("Steam",(floor(Math.random()*100000)).toString(),"Testing123","000000000", 123456))
    }

    fun iterateAndGetOtp() {
        _myList.value?.forEachIndexed { index, items ->
            updateOTP(items.id)
        }
    }

    fun updateOTP(itemID: String) {
        val updatedList = _myList.value?.map {
            if(it.id == itemID) {
                it.copy(otp = t1.GenerateOTP(it.data,""))
            } else {
                it
            }
        }
        _myList.value = updatedList!!
    }

    fun tickerFlow() = flow<Int> {
        val startingValue = (30-((System.currentTimeMillis()/1000)%30))
        var currentValue = startingValue.toInt()
        emit(currentValue)
        while(true) {
            delay(1000L)
            currentValue--
            emit(currentValue)
            if(currentValue==0) {currentValue = 30}
        }
    }

    fun collectFlow() {
        viewModelScope.launch {
            tickerFlow().collect { time ->
                System.out.println(time==0)
                if(time==0) iterateAndGetOtp()
            }
        }
    }
}