package fsrss

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class FeedHandlerTests {
    @Test
    fun createSimpleFeed() {
        val input = Feed(FeedMetaData("title", "desc", "link"), 
                listOf(Item("title1", "link1", "c29tZSBlbmNvZGVkIGRlc2NyaXB0aW9u"),
                        Item("title2", "link2", "c29tZSBlbmNvZGVkIGRlc2NyaXB0aW9u")))

        val output: String = FeedHandler().createRssFeed(input)

        val feed: String = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<rss version=\"2.0\">" +
                "<channel>" +
                "<title>title</title>" +
                "<link>link</link>" +
                "<description>desc</description>" +
                "<item>" +
                "<title>title1</title>" +
                "<link>link1</link>" +
                "<description><![CDATA[some encoded description]]></description>" +
                "</item>" +
                "<item>" +
                "<title>title2</title>" +
                "<link>link2</link>" +
                "<description><![CDATA[some encoded description]]></description>" +
                "</item>" +
                "</channel>" +
                "</rss>"
        Assert.assertEquals(feed,  output)
    }
}