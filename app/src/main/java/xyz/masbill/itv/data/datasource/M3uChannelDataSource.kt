package xyz.masbill.itv.data.datasource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import xyz.masbill.itv.data.parser.M3uParser
import xyz.masbill.itv.domain.model.Category
import xyz.masbill.itv.domain.model.Channel
import java.net.HttpURLConnection
import java.net.URL

class M3uChannelDataSource(
    private val m3uUrl: String = "https://raw.githubusercontent.com/iptv-org/iptv/refs/heads/master/streams/id.m3u",
    private val logosCsvUrl: String = "https://github.com/iptv-org/database/raw/refs/heads/master/data/logos.csv"
) : ChannelDataSource {

    private var cachedCategories: List<Category>? = null
    private var cachedChannels: List<Channel>? = null

    override suspend fun getCategories(): List<Category> {
        if (cachedCategories == null) {
            fetchAndParse()
        }
        return cachedCategories ?: emptyList()
    }

    override suspend fun getChannels(): List<Channel> {
        if (cachedChannels == null) {
            fetchAndParse()
        }
        return cachedChannels ?: emptyList()
    }

    private suspend fun fetchAndParse() = withContext(Dispatchers.IO) {
        try {
            val logoMap = fetchLogoMap()

            val url = URL(m3uUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.requestMethod = "GET"
            connection.setRequestProperty("User-Agent", "Mozilla/5.0")

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val content = connection.inputStream.bufferedReader().use { it.readText() }
                val (parsedCategories, parsedChannels) = M3uParser.parse(content, logoMap)

                cachedCategories = parsedCategories
                cachedChannels = parsedChannels
                return@withContext
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        cachedCategories = emptyList()
        cachedChannels = emptyList()
    }

    private fun fetchLogoMap(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        try {
            val url = URL(logosCsvUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 8000
            connection.readTimeout = 8000
            connection.setRequestProperty("User-Agent", "Mozilla/5.0")

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                connection.inputStream.bufferedReader().useLines { lines ->
                    lines.forEach { line ->
                        val parts = line.split(",")
                        if (parts.size >= 8) {
                            val channelId = parts[0].trim()
                            val logoUrl = parts.last().trim()
                            if (channelId.isNotEmpty() && logoUrl.startsWith("http")) {
                                map[channelId] = logoUrl
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return map
    }
}
