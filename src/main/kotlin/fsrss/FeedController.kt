package fsrss

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.nio.file.Files
import java.nio.file.Path


@Controller
class FeedController {
    val path: String = System.getenv("RSS_FEEDS_PATH") ?: "/data"
    val jsonMapper = jacksonObjectMapper()

    @GetMapping("/feeds/{feed}")
    fun readFeed(@PathVariable feed: String) : ResponseEntity<String> {
        val path = Path.of(path, feed)
        if (!Files.exists(path)) {
            return ResponseEntity("Feed does not exist", HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(FeedHandler().createRssFeed(path.toFile()), HttpStatus.OK)
    }

    @PostMapping("/feeds/{feed}")
    fun createFeed(@PathVariable feed: String, @RequestBody body: String): ResponseEntity<String> {
        val path = Path.of(path, feed)
        if (Files.exists(path)) {
            return ResponseEntity("Feed already exist", HttpStatus.BAD_REQUEST)
        }
        val meta: FeedMetaData = jsonMapper.readValue(body)
        Files.createDirectory(path)
        val metaFile = Files.createFile(Path.of(path.toString(), "meta.json"))
        Files.writeString(metaFile, jsonMapper.writeValueAsString(meta))
        return ResponseEntity(HttpStatus.CREATED)
    }

    @PostMapping("/feeds/{feed}/item")
    fun addItem(@PathVariable feed:String, @RequestBody body: String): ResponseEntity<String> {
        val path = Path.of(path, feed)
        if (!Files.exists(path)) {
            return ResponseEntity("Feed does not exist", HttpStatus.BAD_REQUEST)
        }
        val item: Item = jsonMapper.readValue(body)
        val sanitizedTitle: String = item.title.filter{it.isLetterOrDigit()}.toLowerCase()
        val itemFile = Path.of(path.toString(), sanitizedTitle)
        if (Files.exists(itemFile))  {
            return ResponseEntity("Item already exist", HttpStatus.BAD_REQUEST)
        }

        Files.writeString(itemFile, jsonMapper.writeValueAsString(item))
        return ResponseEntity(HttpStatus.CREATED)
    }
}
