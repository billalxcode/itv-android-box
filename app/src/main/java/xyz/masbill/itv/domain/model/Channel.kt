package xyz.masbill.itv.domain.model

data class Channel(
    val id: String,
    val name: String,
    val logoUrl: String,
    val streamUrl: String,
    val categoryId: String,
    val description: String = "",
    val headers: Map<String, String> = emptyMap()
)
