package fsrss

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FsRssApplication

fun main(args: Array<String>) {
	runApplication<FsRssApplication>(*args)
}
