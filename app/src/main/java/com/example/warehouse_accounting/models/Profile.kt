package com.example.warehouse_accounting.models

import android.net.Uri

data class Profile(
    var firstName: String,
    var lastName: String,
    var login: String,
    var photoUri: Uri?
)