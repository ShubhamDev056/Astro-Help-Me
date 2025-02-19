package com.androiddev.astrohelpme.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.androiddev.astrohelpme.data.local.AppPreference
import com.androiddev.astrohelpme.utils.api.ApiInterceptor
import com.androiddev.astrohelpme.utils.api.NetworkConnection
import com.androiddev.astrohelpme.utils.helper.ApiData
import com.androiddev.astrohelpme.utils.helper.AutoLogoutUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object  AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context : Context): SharedPreferences =
         PreferenceManager.getDefaultSharedPreferences(context)


    @Provides
    @Singleton
    fun provideNetworkConnection(@ApplicationContext context : Context) : NetworkConnection {
        return NetworkConnection(context)
    }

    @Provides
    @Singleton
    fun provideApiInterceptor(appPreference: AppPreference, networkConnection: NetworkConnection) : ApiInterceptor {
        return ApiInterceptor(appPreference,networkConnection)
    }

    @Provides
    @Singleton
    fun provideAutoLogoutUtil(@ApplicationContext context: Context,appPreference: AppPreference): AutoLogoutUtil {
        return AutoLogoutUtil(context,appPreference)
    }

    @Provides
    @Singleton
    fun provideApiData(@ApplicationContext context: Context,appPreference: AppPreference) : ApiData {
        return ApiData(context,appPreference)
    }
}