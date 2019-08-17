package fsrss

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.nio.file.Path

class FeedHandler {
    fun getFeed(path: File): Feed {
        val metaFile: File = Path.of(path.toString(), "meta.json").toFile()
        val feedMeta: FeedMetaData = jacksonObjectMapper().readValue(metaFile)
        // TODO : check on path if files exists / review filter on name
        val feedItems: List<Item> = path.listFiles().filter { !it.endsWith("meta.json") }.map { jacksonObjectMapper().readValue<Item>(it) }
        return Feed(feedMeta, feedItems)
    }

    fun createRssFeed(path: File): String {
        return createRssFeed(getFeed(path))
    }

    fun createRssFeed(feed: Feed): String {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<rss version=\"2.0\">" +
                "<channel>" +
                "<title>${feed.metaData.title}</title>" +
                "<link>${feed.metaData.link}</link>" +
                "<description>${feed.metaData.description}</description>" +
                createRssItems(feed.items) +
                "</channel>" +
                "</rss>"
    }

    private fun createRssItems(items: List<Item>): String {
        var str = ""
        for(item in items) {
            str += "<item>" +
                    "<title>${item.title}</title>" +
                    "<link>${item.link}</link>" +
                    "<description><![CDATA[${item.description}]]></description>" +
                    "</item>"
        }
        return str
    }
}