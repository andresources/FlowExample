package com.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MyRepo constructor(private  val INIT_DELAY: Long = 20000L,private  val DELAY: Long = 1000L) {
    val dataFlow : Flow<Int> = flow{
        delay(INIT_DELAY)
        var num = 0
        while (true){
            num = num.plus(1)
            emit(num)
            delay(DELAY)
        }
    }
}