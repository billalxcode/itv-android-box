package xyz.masbill.itv.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import xyz.masbill.itv.domain.model.Category
import xyz.masbill.itv.domain.model.Channel
import xyz.masbill.itv.domain.usecase.GetCategoriesUseCase
import xyz.masbill.itv.domain.usecase.GetChannelsUseCase

class HomeViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getChannelsUseCase: GetChannelsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val categories = getCategoriesUseCase()
            val initialCategory = categories.firstOrNull()
            val channels = getChannelsUseCase(initialCategory?.id)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    categories = categories,
                    selectedCategory = initialCategory,
                    channels = channels
                )
            }
        }
    }

    fun selectCategory(category: Category) {
        viewModelScope.launch {
            val channels = getChannelsUseCase(category.id)
            _uiState.update {
                it.copy(
                    selectedCategory = category,
                    channels = channels
                )
            }
        }
    }

    fun playChannel(channel: Channel) {
        _uiState.update { it.copy(selectedChannelForPlayer = channel) }
    }

    fun closePlayer() {
        _uiState.update { it.copy(selectedChannelForPlayer = null) }
    }
}
