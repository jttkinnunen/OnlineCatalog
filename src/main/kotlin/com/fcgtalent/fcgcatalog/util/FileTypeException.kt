package com.fcgtalent.fcgcatalog.util

import java.lang.Exception

class FileTypeException(message: String = "You may only upload JPEG or PNG images"): Exception(message) {

}