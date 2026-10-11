package xyz.masbill.itv.domain.usecase

import xyz.masbill.itv.domain.model.Channel
import xyz.masbill.itv.domain.repository.ChannelRepository

class GetChannelsUseCase(
    private val repository: ChannelRepository
) {
    suspend operator fun invoke(categoryId: String? = null): List<Channel> {
        return if (categoryId.isNullOrBlank() || categoryId == "all") {
            repository.getChannels()
        } else {
            repository.getChannelsByCategory(categoryId)
        }
    }
}
