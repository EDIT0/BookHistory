package com.ejstudio.bookhistory.presentation.di

import android.app.Application
import androidx.room.Room
import com.ejstudio.bookhistory.BuildConfig
import com.ejstudio.bookhistory.data.api.ApiClient
import com.ejstudio.bookhistory.data.api.ApiInterface
import com.ejstudio.bookhistory.data.db.MyBookDatabase
import com.ejstudio.bookhistory.data.repository.login.LoginRepositorylmpl
import com.ejstudio.bookhistory.data.repository.login.local.LoginLocalDataSource
import com.ejstudio.bookhistory.data.repository.login.local.LoginLocalDataSourcelmpl
import com.ejstudio.bookhistory.data.repository.login.remote.LoginRemoteDataSource
import com.ejstudio.bookhistory.data.repository.login.remote.LoginRemoteDataSourcelmpl
import com.ejstudio.bookhistory.domain.repository.LoginRepository
import com.ejstudio.bookhistory.domain.usecase.*
import com.ejstudio.bookhistory.presentation.view.activity.LoginActivity
import com.ejstudio.bookhistory.presentation.view.activity.SignUpActivity
import com.ejstudio.bookhistory.presentation.view.activity.SplashActivity
import com.ejstudio.bookhistory.presentation.view.viewmodel.*
import com.ejstudio.bookhistory.util.LoginManager
import com.ejstudio.bookhistory.util.NetworkManager
import com.ejstudio.bookhistory.util.PreferenceManager
import com.kakao.sdk.common.KakaoSdk
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.logging.Level

class KoinApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Kakao SDK 초기화
        KakaoSdk.init(this, "26c4f514d1de68a24b14951564a57d9b")

        startKoin {
            //inject Android context
            androidContext(this@KoinApplication)
            androidLogger(if (BuildConfig.DEBUG) org.koin.core.logger.Level.ERROR else org.koin.core.logger.Level.NONE)
            modules(networkModule)
            modules(apiModule)
            modules(viewModelModule)
            modules(useCaseModule)
            modules(repositoryModule)
            modules(localDataModule)
            modules(
                remoteDataModule,
                activityMoudel
            )
        }
    }
}

val networkModule: Module = module {
    single { NetworkManager(get()) }
}

val apiModule: Module = module {

    single<ApiInterface> { get<Retrofit>().create(ApiInterface::class.java) }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(ApiClient.BASE_URL)
            .client(get())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<GsonConverterFactory> { GsonConverterFactory.create() }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .run {
                addInterceptor(get<Interceptor>())
                build()
            }
    }

    single<Interceptor> {
        Interceptor { chain ->
            with(chain) {
                val newRequest = request().newBuilder()
//                    .addHeader("X-Naver-Client-Id", "33chRuAiqlSn5hn8tIme")
//                    .addHeader("X-Naver-Client-Secret", "fyfwt9PCUN")
//                    .addHeader("Connection", "close")
                    .build()
                proceed(newRequest)
            }
        }
    }
}

val viewModelModule: Module = module {
    viewModel { SplashViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { SignUp2ViewModel(get(), get()) }
    viewModel { FindPasswordViewModel(get(), get()) }
}

val useCaseModule: Module = module {
    single { IsFirstWelcomeUseCase(get()) }
    single { IsAutoLoginUseCase(get()) }
    single { IsLoginAuthUseCase(get()) }
    single { CreateEmailUserUseCase(get()) }
    single { SendSignUpEmailUseCase(get()) }
    single { CheckEmailUseCase(get()) }
    single { RegisterEmailAndPasswordUseCase(get()) }
    single { FindPassword2ViewModel() }
    single { SendFindPasswordEmailUseCase(get()) }
}

val repositoryModule: Module = module {
    single<LoginRepository> { LoginRepositorylmpl(get(), get()) }
}

val localDataModule: Module = module {
    single { PreferenceManager(get()) }
    single<LoginLocalDataSource> { LoginLocalDataSourcelmpl(get()) }
    single<MyBookDatabase> {
        Room.databaseBuilder(get(), MyBookDatabase::class.java, "MyBookDatabase").build()
    }
}

val remoteDataModule: Module = module {
//    single { EmailLogin() }
    single { LoginManager(get()) }
    single<LoginRemoteDataSource> { LoginRemoteDataSourcelmpl(get(), get(), get()) }
}

val activityMoudel: Module = module {
    single { SignUpActivity() }
    single { LoginActivity() }
    single { SplashActivity() }
}