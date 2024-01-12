package com.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MyViewModel(private val repo: MyRepo = MyRepo()) : ViewModel() {
    var _UiData = MutableStateFlow<EvenNumber>(EvenNumber.Loading)
    var UiData: StateFlow<EvenNumber>  = _UiData
    init {
        viewModelScope.launch {
            repo.dataFlow
                .onEach {
                _UiData.value = EvenNumber.EACHITEM("EachItem : $it")
            }.filter {
                 it % 5==0
            }.catch {
                _UiData.value = EvenNumber.FAILED(it)
            }.collectLatest {
                _UiData.value = EvenNumber.SUCCESS(it)
            }
        }
    }
    sealed class EvenNumber{
        object Loading : EvenNumber()
        data class EACHITEM(val num: String) : EvenNumber()
        data class SUCCESS(val num: Int) : EvenNumber()
        data class FAILED(val e: Throwable) : EvenNumber()
    }
}