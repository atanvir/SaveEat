package com.saveeat.di

import com.saveeat.repository.remote.Google.GoogleConstant.GOOGLE_BASE_URL
import com.saveeat.repository.remote.Google.GoogleInterface
import com.saveeat.repository.remote.SaveEat.SaveEatConstant.SAVEEAT_TESTING_BASE_URL
import com.saveeat.repository.remote.SaveEat.SaveEatInterface
import com.saveeat.repository.remote.StaticContent.StaticContentConstant.STATIC_CONTENT_BASE_URL
import com.saveeat.repository.remote.StaticContent.StaticContentInterface
import com.saveeat.utils.application.KeyConstants.GOOGLE_CLIENT
import com.saveeat.utils.application.KeyConstants.SAVEEAT_CLIENT
import com.saveeat.utils.application.KeyConstants.STATIC_CONTENT_CLIENT
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Reusable
    internal fun provideSaveEatClient(builder:OkHttpClient.Builder) : SaveEatInterface {
        return Retrofit.Builder()
            .baseUrl(SAVEEAT_TESTING_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(builder.build())
            .build().create(SaveEatInterface::class.java)
    }


    @Provides
    @Reusable
    internal fun provideStaticContentClient(builder:OkHttpClient.Builder) : StaticContentInterface {
        return Retrofit.Builder()
            .baseUrl(STATIC_CONTENT_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(builder.build())
            .build().create(StaticContentInterface::class.java)
    }



    @Provides
    @Reusable
    internal fun provideGoogleClient(builder:OkHttpClient.Builder) : GoogleInterface {
        return Retrofit.Builder()
            .baseUrl(GOOGLE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(builder.build())
            .build().create(GoogleInterface::class.java)
    }


    @Provides
    @Reusable
    internal fun provideOkhttpClientBuilder(): OkHttpClient.Builder {
        val okHttpClientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        okHttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(50, TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(60, TimeUnit.SECONDS)
        okHttpClientBuilder.addInterceptor(loggingInterceptor)
        return okHttpClientBuilder
    }


}