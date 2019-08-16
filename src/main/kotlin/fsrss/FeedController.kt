package fsrss

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import java.io.File
import java.nio.file.Files
import java.nio.file.Path


@Controller
class FeedController {
    val path: String = System.getenv("RSS_FEEDS_PATH") ?: "/"
    val jsonMapper = jacksonObjectMapper()

    @GetMapping("/feeds/{feed}")
    @ResponseBody
    fun readFeed(@PathVariable feed: String) : String {
        val path = Path.of(path, feed)
        if (Files.exists(path)) {
            return FeedHandler().createRssFeed(path.toFile())
        } else {
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "Feed does not exist")
        }
    }

    @RequestMapping("/feeds/{feed}")
    @ResponseStatus(HttpStatus.OK)
    fun createFeed(@PathVariable feed: String, @RequestBody body: String) {
        val path = Path.of(path + feed)
        if (!Files.exists(path)) {
            val meta: FeedMetaData = jsonMapper.readValue(body)
            Files.createDirectory(path)
            val metaFile = File("$path/meta.json")
            metaFile.createNewFile()
            metaFile.writeText(jsonMapper.writeValueAsString(meta))
        } else {
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "Feed already exist")
        }
    }

    @RequestMapping("/feeds/{feed}/item")
    @ResponseStatus(HttpStatus.OK)
    fun addItem(@PathVariable feed:String, @RequestBody body: String) {
        val path = Path.of(path, feed)
        if (Files.exists(path)) {
            val item: Item = jsonMapper.readValue(body)
            val sanitizedTitle: String = item.title.filter{it.isLetterOrDigit()}.toLowerCase()
            val itemFile = File(path.toFile(), sanitizedTitle)
            if (!itemFile.exists())  {
                itemFile.createNewFile()
                itemFile.writeText(jsonMapper.writeValueAsString(item))
            } else {
                throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "Item already exist")
            }
        } else {
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "Feed does not exist")
        }
    }
}