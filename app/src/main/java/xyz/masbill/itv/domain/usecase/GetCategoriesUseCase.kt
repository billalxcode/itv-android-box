package xyz.masbill.itv.domain.usecase

import xyz.masbill.itv.domain.model.Category
import xyz.masbill.itv.domain.repository.ChannelRepository

class GetCategoriesUseCase(
    private val repository: ChannelRepository
) {
    suspend operator fun invoke(): List<Category> {
        return repository.getCategories()
    }
}
