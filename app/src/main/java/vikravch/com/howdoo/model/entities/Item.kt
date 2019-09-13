package vikravch.com.howdoo.model.entities

import com.squareup.moshi.Json

class Item (
    @field:Json(name = "name") val name:String,
    @field:Json(name = "full_name") val fullName:String,
    @field:Json(name = "owner") val owner:Owner,
    @field:Json(name = "updated_at") val udatedAt:String
)