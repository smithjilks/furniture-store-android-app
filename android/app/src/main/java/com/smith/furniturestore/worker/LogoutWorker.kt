package com.smith.furniturestore.worker

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.smith.furniturestore.data.repository.FurnitureRepository
import com.smith.furniturestore.ui.AuthActivity
import java.lang.Exception
import java.time.LocalDateTime
import java.util.*

class LogoutWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    private val sharedPrefFile = "com.smith.furniturestore.user"

    override fun doWork(): Result {
            val userPreferences =
                applicationContext.getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
            val tokenExpiry =
                userPreferences.getString(AuthActivity.TOKEN_EXPIRY, "2022-04-17T14:07:41.728834")
            val tokenExpiryTimeStamp = LocalDateTime.parse(tokenExpiry)

            if (LocalDateTime.now().isAfter(tokenExpiryTimeStamp)) {
               return Result.success(workDataOf(AuthActivity.TOKEN_EXPIRY_STATUS to "invalid"))
            }
            return Result.success(workDataOf(AuthActivity.TOKEN_EXPIRY_STATUS to "valid"))

    }

}