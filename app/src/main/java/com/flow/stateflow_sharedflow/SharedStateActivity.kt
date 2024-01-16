package com.flow.stateflow_sharedflow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.flow.databinding.ActivitySharedStateBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class SharedStateActivity : AppCompatActivity() {
    var _binding: ActivitySharedStateBinding? =null
    val binding: ActivitySharedStateBinding get() = _binding!!

    //1. StateFlow
    /*
    is a state-holder observable flow, adv. version of LiveData.
    It always has an initial value and only stores the latest emitted value.
    It uses value property to update the stream.
     It is also a conflated flow, meaning that when a new value is emitted, the most recent value is retained and immediately emitted to new collectors.
     */

    var mutableStateFlow: MutableStateFlow<Int> = MutableStateFlow(-100)
    //Expose a read-only version of your mutable flow to prevent external changes.
    val stateFlow: StateFlow<Int> = mutableStateFlow

    //2. SharedFlow
    /*
    A SharedFlow is a hot flow that can have multiple collectors.It is used, when you want to have multiple subscribers to the same stream of data.
    It does not have an initial value.It uses emit or tryEmit fun. to update the stream.
    You can configure its replay cache to store a certain number of previously emitted values for new collectors.


     */
    var mutableSharedFlow: MutableSharedFlow<Int> = MutableSharedFlow<Int>()
    val sharedFlow = mutableSharedFlow.asSharedFlow() // or  val sharedFlow:  SharedFlow<Int> = mutableSharedFlow
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySharedStateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding){
            btnStateFlow.setOnClickListener {
                lifecycleScope.launch {
                    getStateFlowStream()
                }
            }

            btnSharedFlow.setOnClickListener {
                lifecycleScope.launch {
                    getSharedFlowStream()
                }
            }
        }
        //StateFlow - Collectors
         val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            println("Handle $exception in CoroutineExceptionHandler")
        }
        val scope = CoroutineScope(Dispatchers.Main + SupervisorJob() + coroutineExceptionHandler)
        scope.launch {
            stateFlow.catch {
                    binding.tv1.text = "Exception..."
                }
                .collectLatest {
                    binding.tv1.text = "StateFlow - Collector 1 received: $it"
                 }
        }
        scope.launch {
            stateFlow
                .catch {
                binding.tv2.text = "Exception..."
            }.collectLatest {
                binding.tv2.text = "StateFlow - Collector 2 received: $it"
            }
        }

        //Shared - Collectors
        lifecycleScope.launch {
            sharedFlow.collectLatest {
                binding.tv1.text = "SharedFlow - Collector 1 received: $it"
            }
        }
        lifecycleScope.launch {
            sharedFlow.collectLatest {
                binding.tv2.text = "SharedFlow - Collector 2 received: $it"
            }
        }
    }

    private suspend fun getStateFlowStream(){
        delay(10000L)
        repeat(5){
            mutableStateFlow.value = it
            delay(3000L)
        }


    }
    private suspend fun getSharedFlowStream(){
        delay(10000L)
        repeat(5){
            mutableSharedFlow.emit(it)
            //mutableSharedFlow.tryEmit()
            delay(3000L)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}