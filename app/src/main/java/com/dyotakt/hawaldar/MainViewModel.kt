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

    private var map: MutableMap<String, *>? = dataMan.getAllData()

    private val _myList = MutableLiveData<List<items>>(listOf(
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
//        _myList.value = _myList.value?.plus(items("X",(floor(Math.random()*100000)).toString(),"Testing123456","010010010", 123456))
//        _myList.value = _myList.value?.plus(
//            items(
//                "DataMan",
//                (floor(Math.random()*100000)).toString(), //UID Generation
//                dataMan.getString("data_key","Testing12345"), //Pulling the key from SharedPreferences
//                "010010010",
//                123456
//            )
//        )
        map = dataMan.getAllData()
        map?.forEach { entry ->
            _myList.value = _myList.value?.plus(
                items(
                    entry.key,
                    (floor(Math.random()*100000)).toString(), //UID Generation
                    entry.value.toString(),
                    "010010010",
                    t1.GenerateOTP(entry.value.toString(),"")
                )
            )
        }

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
                System.out.println(time == 0)
                if (time == 0) iterateAndGetOtp()
            }
        }
    }
}