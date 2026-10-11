package xyz.masbill.itv.data.parser

import xyz.masbill.itv.domain.model.Category
import xyz.masbill.itv.domain.model.Channel

object M3uParser {

    fun parse(m3uContent: String, logoMap: Map<String, String> = emptyMap()): Pair<List<Category>, List<Channel>> {
        val lines = m3uContent.lines()
        val channels = mutableListOf<Channel>()

        var currentTvgId: String? = null
        var currentTvgLogo: String? = null
        var currentTitle: String? = null
        var currentGroupTitle: String? = null
        val currentHeaders = mutableMapOf<String, String>()

        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.isEmpty()) continue

            if (trimmed.startsWith("#EXTINF:")) {
                currentTvgId = extractAttribute(trimmed, "tvg-id")
                currentTvgLogo = extractAttribute(trimmed, "tvg-logo")
                currentGroupTitle = extractAttribute(trimmed, "group-title")

                val commaIndex = trimmed.lastIndexOf(',')
                if (commaIndex != -1) {
                    val rawTitle = trimmed.substring(commaIndex + 1).trim()
                    currentTitle = cleanTitle(rawTitle)
                }
            } else if (trimmed.startsWith("#EXTVLCOPT:")) {
                val opt = trimmed.removePrefix("#EXTVLCOPT:").trim()
                if (opt.startsWith("http-user-agent=")) {
                    currentHeaders["User-Agent"] = opt.removePrefix("http-user-agent=").trim()
                } else if (opt.startsWith("http-referrer=")) {
                    currentHeaders["Referer"] = opt.removePrefix("http-referrer=").trim()
                }
            } else if (!trimmed.startsWith("#")) {
                if (!currentTitle.isNullOrBlank()) {
                    val category = determineCategory(currentGroupTitle, currentTitle)
                    val rawTvgId = currentTvgId ?: ""
                    val cleanTvgId = rawTvgId.split("@")[0].trim()

                    val logo = when {
                        !currentTvgLogo.isNullOrBlank() -> currentTvgLogo
                        logoMap.containsKey(cleanTvgId) -> logoMap[cleanTvgId] ?: ""
                        logoMap.containsKey(rawTvgId) -> logoMap[rawTvgId] ?: ""
                        else -> ""
                    }

                    channels.add(
                        Channel(
                            id = "ch_${channels.size + 1}",
                            name = currentTitle,
                            logoUrl = logo,
                            streamUrl = trimmed,
                            categoryId = category,
                            description = "Siaran TV Indonesia - $currentTitle",
                            headers = currentHeaders.toMap(),
                        )
                    )
                }

                currentTvgId = null
                currentTvgLogo = null
                currentTitle = null
                currentGroupTitle = null
                currentHeaders.clear()
            }
        }

        val categoryMap = mutableMapOf<String, Category>()
        categoryMap["all"] = Category("all", "Semua")

        for (channel in channels) {
            val catName = when (channel.categoryId) {
                "news" -> "Berita"
                "national" -> "Nasional"
                "regional" -> "Daerah"
                "religion" -> "Religi"
                else -> "Lainnya"
            }
            categoryMap[channel.categoryId] = Category(channel.categoryId, catName)
        }

        return Pair(categoryMap.values.toList(), channels)
    }

    private fun extractAttribute(line: String, attributeName: String): String? {
        val pattern = Regex("""$attributeName="([^"]*)"""")
        val match = pattern.find(line)
        return match?.groupValues?.get(1)
    }

    private fun cleanTitle(title: String): String {
        return title
            .replace(Regex("""\((1080p|720p|480p|360p|SD|HD|FHD|4K|2K|576p|576i)\)""", RegexOption.IGNORE_CASE), "")
            .replace(Regex("\\[.*?]"), "")
            .trim()
    }

    private fun determineCategory(groupTitle: String?, title: String): String {
        val combined = "${groupTitle ?: ""} $title".lowercase()
        return when {
            combined.contains("news") || combined.contains("berita") || combined.contains("kompas") || combined.contains("metro") || combined.contains("cnn") || combined.contains("cnbc") || combined.contains("inews") || combined.contains("tvone") -> "news"
            combined.contains("tvri") && combined.length > 8 -> "regional"
            combined.contains("tvri") -> "national"
            combined.contains("iman") || combined.contains("bahjah") || combined.contains("rodja") || combined.contains("islam") || combined.contains("religi") -> "religion"
            combined.contains("trans") || combined.contains("antv") || combined.contains("rtv") || combined.contains("net") || combined.contains("gtv") || combined.contains("indosiar") || combined.contains("sctv") || combined.contains("rcti") || combined.contains("mnc") -> "national"
            else -> "national"
        }
    }
}
