package cz.applifting.humansis.model.api

import com.google.gson.annotations.SerializedName

class GetSaltResponse(@SerializedName("user_id") val userId: Int, @SerializedName("salt") val salt: String)