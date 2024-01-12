package com.flow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.flow.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? =null
    private val binding: ActivityMainBinding get() = _binding!!
    lateinit var viewModel: MyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        with(binding) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED){
                    viewModel.UiData.collectLatest {
                        when(it){
                            is MyViewModel.EvenNumber.Loading->{
                                tv.visible(false)
                                tvEveryItem.text = "Loading From Server...."
                            }
                            is MyViewModel.EvenNumber.EACHITEM-> tvEveryItem.text = it.num
                            is MyViewModel.EvenNumber.SUCCESS -> {
                                tv.visible(true)
                                tv.text = "Num: ${it.num}"
                            }
                            is MyViewModel.EvenNumber.FAILED -> tv.text = "Num: ${it.e.message}"
                        }
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}