package com.example.e_wastehubkenya.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_wastehubkenya.BuildConfig
import com.example.e_wastehubkenya.data.Resource
import com.example.e_wastehubkenya.data.remote.DashboardRetrofitInstance
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.IOException

class SerialNumberViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val stolenDevicesCollection = db.collection("stolen_devices")

    private val _verificationResult = MutableLiveData<Resource<Boolean>>()
    val verificationResult: LiveData<Resource<Boolean>> = _verificationResult

    fun verifySerialNumber(serialNumber: String) {
        _verificationResult.postValue(Resource.Loading())

        viewModelScope.launch {
            try {
                val document = stolenDevicesCollection.document(serialNumber).get().await()

                if (document.exists()) {
                    _verificationResult.postValue(Resource.Error("Serial number reported as stolen"))
                } else {
                    _verificationResult.postValue(verifyWithExternalApi(serialNumber))
                }
            } catch (e: Exception) {
                _verificationResult.postValue(Resource.Error(e.message ?: "An error occurred checking our database"))
            }
        }
    }

    private suspend fun verifyWithExternalApi(serialNumber: String): Resource<Boolean> {
        return try {
            val response = DashboardRetrofitInstance.api.checkImei(serialNumber, BuildConfig.IMEI_API_KEY)
            if (response.isSuccessful) {
                if (response.body()?.success == true) {
                    Resource.Success(true)
                } else {
                    Resource.Error("Serial number is not valid.")
                }
            } else {
                Resource.Error("Could not be verified (error code: ${response.code()})")
            }
        } catch (e: IOException) {
            Resource.Error("Could not be verified. Please check your internet connection.")
        } catch (e: Exception) {
            Resource.Error("An unexpected error occurred during verification.")
        }
    }
}
