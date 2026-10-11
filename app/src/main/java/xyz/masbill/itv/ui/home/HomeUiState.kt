package xyz.masbill.itv.ui.home

import xyz.masbill.itv.domain.model.Category
import xyz.masbill.itv.domain.model.Channel

data class HomeUiState(
    val isLoading: Boolean = true,
    val categories: List<Category> = emptyList(),
    val selectedCategory: Category? = null,
    val channels: List<Channel> = emptyList(),
    val selectedChannelForPlayer: Channel? = null
)
