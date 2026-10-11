package xyz.masbill.itv.domain.repository

import xyz.masbill.itv.domain.model.Category
import xyz.masbill.itv.domain.model.Channel

interface ChannelRepository {
    suspend fun getCategories(): List<Category>
    suspend fun getChannels(): List<Channel>
    suspend fun getChannelsByCategory(categoryId: String): List<Channel>
}
