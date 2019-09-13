package vikravch.com.howdoo.model.entities

import com.squareup.moshi.Json

class GitResponse (
    @field:Json(name = "items") val items:List<Item>
)