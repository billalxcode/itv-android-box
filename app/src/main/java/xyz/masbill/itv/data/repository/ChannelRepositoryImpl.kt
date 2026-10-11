package xyz.masbill.itv.data.repository

import xyz.masbill.itv.data.datasource.ChannelDataSource
import xyz.masbill.itv.domain.model.Category
import xyz.masbill.itv.domain.model.Channel
import xyz.masbill.itv.domain.repository.ChannelRepository

class ChannelRepositoryImpl(
    private val dataSource: ChannelDataSource
) : ChannelRepository {

    override suspend fun getCategories(): List<Category> {
        return dataSource.getCategories()
    }

    override suspend fun getChannels(): List<Channel> {
        return dataSource.getChannels()
    }

    override suspend fun getChannelsByCategory(categoryId: String): List<Channel> {
        return dataSource.getChannels().filter { it.categoryId == categoryId }
    }
}
