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
    private var map: MutableMap<String, *>? = dataMan.getAllData()

    private var _myList = MutableLiveData<List<items>>(listOf(
        items("Steam",(floor(Math.random()*100000)).toString(),"Testing123","023026033","steam", 123456),
        items("Google",(floor(Math.random()*100000)).toString(),"Testing1234","234234234","google",123456),
        items("Facebook",(floor(Math.random()*100000)).toString(),"Testing12345","059089152", "facebook",123456)

    ))
    val myList: LiveData<List<items>> = _myList



    fun fetchAndPopulateData() {
        map = dataMan.getAllData()
//        System.out.println("MAP"+map);
        map?.forEach { entry ->
           _myList.value = _myList.value?.plus(
                items(
                    entry.key,
                    (floor(Math.random()*100000)).toString(), //UID Generation
                    entry.value.toString(),
                    "010010010",
                    "",
                    t1.GenerateOTP(entry.value.toString(),"")
                )
            )
        }
    }

    val t1: totp = totp()

    init {
        fetchAndPopulateData()
        iterateAndGetOtp()
        collectFlow()
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

    fun iterateAndGetOtp() {
        _myList.value?.forEachIndexed { index, items ->
            updateOTP(items.id)
        }
    }

//    fun iterateAndGetOtp() {
//        _myList.value.keys { index:String, items:items ->
//            updateOTP(items.id)
//        }
//        for(currentKey in _myList.value?.keys!!) {
//            _myList.value!!.get(currentKey)?.let { updateOTP(it.id) }
//        }
//    }

    fun updateOTP(itemID: String) {
        val updatedList = _myList.value?.map {
            if(it.id == itemID) {
                it.copy(otp = t1.GenerateOTP(it.data,"")) //key is empty as it will take the current GMT time for it
            } else {
                it
            }
        }
        _myList.value = updatedList!!
    }
}