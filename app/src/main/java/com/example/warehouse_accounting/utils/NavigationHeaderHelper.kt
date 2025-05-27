package com.example.warehouse_accounting.utils

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.models.Profile

class NavigationHeaderHelper {

    companion object {
        fun updateNavigationHeader(
            profile: Profile?,
            profilePicture: ImageView,
            profileName: TextView,
            profileEmail: TextView
        ) {
            if (profile != null) {
                val displayName = when {
                    !profile.firstName.isNullOrEmpty() && !profile.lastName.isNullOrEmpty() ->
                        "${profile.firstName} ${profile.lastName}"
                    !profile.firstName.isNullOrEmpty() -> profile.firstName
                    !profile.lastName.isNullOrEmpty() -> profile.lastName
                    !profile.login.isNullOrEmpty() -> profile.login
                    else -> "Пользователь"
                }
                profileName.text = displayName

                profileEmail.text = profile.email ?: "Не указан"

                loadProfileImage(profile.photoUri, profilePicture)
            } else {
                profileName.text = "Не авторизован"
                profileEmail.text = ""
                profilePicture.setImageResource(R.mipmap.ic_launcher_round)
            }
        }

        private fun loadProfileImage(photoUri: String?, imageView: ImageView) {
            if (!photoUri.isNullOrEmpty()) {
                Glide.with(imageView.context)
                    .load(photoUri)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .circleCrop()
                    .into(imageView)
            } else {
                imageView.setImageResource(R.mipmap.ic_launcher_round)
            }
        }
    }
}
