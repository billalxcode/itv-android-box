package xyz.masbill.itv.data.datasource

import xyz.masbill.itv.domain.model.Category
import xyz.masbill.itv.domain.model.Channel

interface ChannelDataSource {
    suspend fun getCategories(): List<Category>
    suspend fun getChannels(): List<Channel>
}
