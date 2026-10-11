package xyz.masbill.itv.data.datasource

import xyz.masbill.itv.domain.model.Category
import xyz.masbill.itv.domain.model.Channel

class SampleChannelDataSource : ChannelDataSource {

    private val categories = listOf(
        Category(id = "all", name = "Semua"),
        Category(id = "news", name = "Berita"),
        Category(id = "entertainment", name = "Hiburan"),
        Category(id = "sports", name = "Olahraga"),
        Category(id = "movies", name = "Film"),
        Category(id = "music", name = "Musik")
    )

    private val channels = listOf(
        Channel(
            id = "ch_1",
            name = "Big Buck Bunny (HLS)",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/c/c5/Big_buck_bunny_poster_big.jpg",
            streamUrl = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8",
            categoryId = "movies",
            description = "Channel film animasi 4K Big Buck Bunny streaming via HLS."
        ),
        Channel(
            id = "ch_2",
            name = "Apple Test Stream",
            logoUrl = "https://developer.apple.com/assets/elements/icons/hls/hls-96x96.png",
            streamUrl = "https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_fmp4/master.m3u8",
            categoryId = "news",
            description = "Siaran berita dan uji tes kualitas video Apple Advanced HLS."
        ),
        Channel(
            id = "ch_3",
            name = "Sintel Movie Channel",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/8/8f/Sintel_poster.jpg",
            streamUrl = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8",
            categoryId = "movies",
            description = "Saluran bioskop film petualangan Sintel Open Movie Project."
        ),
        Channel(
            id = "ch_4",
            name = "Tears of Steel 4K",
            logoUrl = "https://upload.wikimedia.org/wikipedia/commons/0/0c/Tears_of_Steel_poster.jpg",
            streamUrl = "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8",
            categoryId = "entertainment",
            description = "Hiburan sci-fi dengan visual efek spektakuler."
        ),
        Channel(
            id = "ch_5",
            name = "Live Sports HD",
            logoUrl = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?w=400",
            streamUrl = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8",
            categoryId = "sports",
            description = "Liputan olahraga langsung kualitas HD 60fps."
        ),
        Channel(
            id = "ch_6",
            name = "Music Hits TV",
            logoUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=400",
            streamUrl = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8",
            categoryId = "music",
            description = "Video musik terbaik 24/7 non-stop."
        )
    )

    override suspend fun getCategories(): List<Category> = categories

    override suspend fun getChannels(): List<Channel> = channels
}
