package np.com.hemnath.mylens.data.domain

import com.google.gson.annotations.SerializedName

data class ApiResponse (
    @SerializedName("status_code" ) var statusCode : Int?     = null,
    @SerializedName("success"     ) var success    : Success? = Success(),
    @SerializedName("image"       ) var image      : Image?   = Image(),
    @SerializedName("status_txt"  ) var statusTxt  : String?  = null
)

data class Success (
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("code"    ) var code    : Int?    = null
)

data class Image (
    @SerializedName("name"              ) var name             : String? = null,
    @SerializedName("extension"         ) var extension        : String? = null,
    @SerializedName("size"              ) var size             : Int?    = null,
    @SerializedName("width"             ) var width            : Int?    = null,
    @SerializedName("height"            ) var height           : Int?    = null,
    @SerializedName("date"              ) var date             : String? = null,
    @SerializedName("date_gmt"          ) var dateGmt          : String? = null,
    @SerializedName("storage_id"        ) var storageId        : String? = null,
    @SerializedName("description"       ) var description      : String? = null,
    @SerializedName("nsfw"              ) var nsfw             : String? = null,
    @SerializedName("md5"               ) var md5              : String? = null,
    @SerializedName("storage"           ) var storage          : String? = null,
    @SerializedName("original_filename" ) var originalFilename : String? = null,
    @SerializedName("original_exifdata" ) var originalExifdata : String? = null,
    @SerializedName("views"             ) var views            : String? = null,
    @SerializedName("id_encoded"        ) var idEncoded        : String? = null,
    @SerializedName("filename"          ) var filename         : String? = null,
    @SerializedName("ratio"             ) var ratio            : Double? = null,
    @SerializedName("size_formatted"    ) var sizeFormatted    : String? = null,
    @SerializedName("mime"              ) var mime             : String? = null,
    @SerializedName("bits"              ) var bits             : Int?    = null,
    @SerializedName("channels"          ) var channels         : String? = null,
    @SerializedName("url"               ) var url              : String? = null,
    @SerializedName("url_viewer"        ) var urlViewer        : String? = null,
    @SerializedName("views_label"       ) var viewsLabel       : String? = null,
    @SerializedName("display_url"       ) var displayUrl       : String? = null,
    @SerializedName("how_long_ago"      ) var howLongAgo       : String? = null
)