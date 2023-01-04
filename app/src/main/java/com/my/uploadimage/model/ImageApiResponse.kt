package com.my.uploadimage.model

import java.io.Serializable

class ImageApiResponse : Serializable {
    var success: Boolean? = null
    var message: String? = null
    var statusCode = 0
}