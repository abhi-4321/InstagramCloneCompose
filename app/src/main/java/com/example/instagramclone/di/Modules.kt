package com.example.instagramclone.di

import com.example.instagramclone.network.login.RetrofitInterfaceLogin
import com.example.instagramclone.network.main.RetrofitInterfaceMain
import com.example.instagramclone.network.util.CustomAuthInterceptor
import com.example.instagramclone.viewmodel.ChatViewModel
import com.example.instagramclone.viewmodel.LoginViewModel
import com.example.instagramclone.viewmodel.MainViewModel
import com.example.instagramclone.viewmodel.StoryViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

}

val networkModule = module {

    // Provide OkHttpClient with token passed at runtime
    factory { (token: String) ->
        OkHttpClient.Builder()
            .addInterceptor(CustomAuthInterceptor(token))
            .build()
    }

    // Provide RetrofitInterfaceLogin with token passed at runtime
    factory<RetrofitInterfaceMain> { (token: String) ->
        Retrofit.Builder()
            .baseUrl("https://instagram-clone-3rjr.onrender.com")
            .client(get<OkHttpClient> { parametersOf(token) })
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitInterfaceMain::class.java)
    }

    single<RetrofitInterfaceLogin> {
        Retrofit.Builder()
//            .baseUrl("http://192.168.1.33:3000")
            .baseUrl("https://instagram-clone-3rjr.onrender.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitInterfaceLogin::class.java)
    }
}

val viewmodelModule = module {
    viewModel { (token: String) ->
        MainViewModel(
            get<RetrofitInterfaceMain> { parametersOf(token) }
        )
    }

    viewModel { (userId: Int, token: String) ->
        ChatViewModel(
            userId, get<RetrofitInterfaceMain> { parametersOf(token) }, get()
        )
    }

    viewModel {
        LoginViewModel(
            get()
        )
    }

    viewModel { (token: String) ->
        StoryViewModel(
            get<RetrofitInterfaceMain> { parametersOf(token) }
        )
    }
}