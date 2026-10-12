package com.example.pagerx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pagerx.data.MockPagerRepository
import com.example.pagerx.ui.PagerScreen
import com.example.pagerx.ui.PagerViewModel
import com.example.pagerx.ui.theme.PagerXTheme

class MainActivity : ComponentActivity() {
    private val repository by lazy { MockPagerRepository() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PagerXTheme {
                val vm: PagerViewModel = viewModel(factory = PagerViewModelFactory(repository))
                PagerScreen(vm)
            }
        }
    }
}
private class PagerViewModelFactory(private val repository: MockPagerRepository) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKEDCAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T = PagerViewModel(repository) as T
}
