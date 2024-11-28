package com.androiddev.astrohelpme.ui.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.androiddev.astrohelpme.data.auth.OTPVerify
import com.androiddev.astrohelpme.data.local.AppPreference
import com.androiddev.astrohelpme.data.repository.AuthRepository
import com.androiddev.astrohelpme.data.response.OtpVerifyResponse
import com.androiddev.astrohelpme.utils.api.Resource
import com.androiddev.astrohelpme.utils.helper.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OtpVerifyViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val appPreference: AppPreference,
) : BaseViewModel() {
    var userId: String = AppConstants.EMPTY
    var mobileNumber: String = AppConstants.EMPTY
    var errorMsgObserver = MutableLiveData(AppConstants.EMPTY)


    private val _otpVerifyResponse = MutableLiveData<Resource<OtpVerifyResponse>>()
    val otpVerifyResponseObserver: MutableLiveData<Resource<OtpVerifyResponse>>
        get() = _otpVerifyResponse

    fun onNextClick(view: View) {
        errorMsgObserver.value = AppConstants.EMPTY
        if (!validateLoginInput()) return@onNextClick
        _otpVerifyResponse.value = Resource.Loading<Nothing>()
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiRequest {
                        authRepository.otpVerify(OTPVerify(userId, mobileNumber))
                    }
                }
                Log.d("MYTAG", "onSubmitClick: button clickedd")
                _otpVerifyResponse.value = Resource.Success(response)
            } catch (e: Exception) {
                _otpVerifyResponse.value = Resource.Failure(e)
            }
        }
    }

    fun validateLoginInput(): Boolean {
        val message = if (mobileNumber.isNotEmpty() && mobileNumber.length == 10) {
            if (userId.isNotEmpty()) {
                return true
            } else "user id not found"
        } else "mobile number not found"
        errorMsgObserver.value = message
        Log.d("MYTAG", "validateLoginInput: " + errorMsgObserver.value)
        return false
    }


}