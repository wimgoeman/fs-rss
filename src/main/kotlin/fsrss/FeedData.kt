package fsrss

data class Feed (
    val metaData: FeedMetaData,
    val items: List<Item>
)

data class FeedMetaData(
    val title: String,
    val description: String,
    val link: String
)

data class Item (
    val title: String,
    val link: String,
    val description: String
)
