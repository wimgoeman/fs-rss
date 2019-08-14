package fsrss

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.ResponseBody


@Controller
class FeedController {

    @GetMapping("/feeds/{feed}")
    @ResponseBody
    fun readFeed(@PathVariable feed: String) :  String {
        return "<rss>$feed</rss>"
    }
}