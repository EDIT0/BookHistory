package com.ejstudio.bookhistory.presentation.di

import android.app.Application
import androidx.room.Room
import com.ejstudio.bookhistory.BuildConfig
import com.ejstudio.bookhistory.data.api.ApiClient
import com.ejstudio.bookhistory.data.api.ApiInterface
import com.ejstudio.bookhistory.data.db.*
import com.ejstudio.bookhistory.data.repository.login.LoginRepositorylmpl
import com.ejstudio.bookhistory.data.repository.login.local.LoginLocalDataSource
import com.ejstudio.bookhistory.data.repository.login.local.LoginLocalDataSourcelmpl
import com.ejstudio.bookhistory.data.repository.login.remote.LoginRemoteDataSource
import com.ejstudio.bookhistory.data.repository.login.remote.LoginRemoteDataSourcelmpl
import com.ejstudio.bookhistory.data.repository.main.booklist.BookListRepositorylmpl
import com.ejstudio.bookhistory.data.repository.main.booklist.local.BookListLocalDataSource
import com.ejstudio.bookhistory.data.repository.main.booklist.local.BookListLocalDataSourcelmpl
import com.ejstudio.bookhistory.data.repository.main.booklist.remote.BookListRemoteDataSource
import com.ejstudio.bookhistory.data.repository.main.booklist.remote.BookListRemoteDataSourcelmpl
import com.ejstudio.bookhistory.data.repository.main.booksearch.BookSearchRepositorylmpl
import com.ejstudio.bookhistory.data.repository.main.booksearch.local.SearchBookLocalDataSource
import com.ejstudio.bookhistory.data.repository.main.booksearch.local.SearchBookLocalDataSourcelmpl
import com.ejstudio.bookhistory.data.repository.main.booksearch.remote.remote.SearchBookRemoteDataSource
import com.ejstudio.bookhistory.data.repository.main.booksearch.remote.remote.SearchBookRemoteDataSourcelmpl
import com.ejstudio.bookhistory.domain.repository.BookListRepository
import com.ejstudio.bookhistory.domain.repository.BookSearchRepository
import com.ejstudio.bookhistory.domain.repository.LoginRepository
import com.ejstudio.bookhistory.domain.usecase.*
import com.ejstudio.bookhistory.domain.usecase.login.*
import com.ejstudio.bookhistory.domain.usecase.main.*
import com.ejstudio.bookhistory.domain.usecase.main.booklist.*
import com.ejstudio.bookhistory.domain.usecase.main.booksearch.*
import com.ejstudio.bookhistory.presentation.view.activity.login.LoginActivity
import com.ejstudio.bookhistory.presentation.view.activity.login.SignUpActivity
import com.ejstudio.bookhistory.presentation.view.activity.SplashActivity
import com.ejstudio.bookhistory.presentation.view.fragment.main.BookListFragment
import com.ejstudio.bookhistory.presentation.view.fragment.main.BookSearchFragment
import com.ejstudio.bookhistory.presentation.view.fragment.main.MyBookHistoryFragment
import com.ejstudio.bookhistory.presentation.view.fragment.main.SettingFragment
import com.ejstudio.bookhistory.presentation.view.viewmodel.*
import com.ejstudio.bookhistory.presentation.view.viewmodel.login.*
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booksearch.BookDetailPageViewModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.MainViewModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist.BookViewModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist.SeeTextMemoViewModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist.WriteTextMemoViewModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booksearch.SearchResultViewModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booksearch.SearchViewModel
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
                activityMoudel,
                fragmentModule
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
    viewModel { MainViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { SearchViewModel(get(), get(), get(), get(), get()) }
    viewModel { SearchResultViewModel(get(), get(), get()) }
    viewModel { BookDetailPageViewModel(get()) }
    viewModel { BookViewModel(get(), get(), get(), get()) }
    viewModel { WriteTextMemoViewModel(get()) }
    viewModel { SeeTextMemoViewModel(get(), get()) }
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
    single { GetSearchBookUseCase(get()) }
    single { InsertRecentSearchesUseCase(get()) }
    single { GetRecentSearchesUseCase(get()) }
    single { DeleteRecentSearchesUseCase(get()) }
    single { TotalDeleteRecentSearchesUseCase(get()) }
    single { AddBookUseCase(get()) }
    single { GetBeforeReadBookUseCase(get()) }
    single { GetReadingBookUseCase(get()) }
    single { GetEndReadBookUseCase(get()) }
    single { GetRecentPopularBookUseCase(get()) }
    single { GetRecommendBookUseCase(get()) }
    single { GetTotalBookUseCase(get()) }
    single { GetAlwaysPopularBookUseCase(get()) }
    single { GetIdxBookInfoUseCase(get()) }
    single { DeleteIdxBookInfoUseCase(get()) }
    single { UpdateBookReadingStateUseCase(get()) }
    single { GetTextMemoUseCase(get()) }
    single { InsertTextMemoUseCase(get()) }
    single { GetIdxTextMemoUseCase(get()) }
    single { DeleteIdxTextMemoUseCase(get()) }
}

val repositoryModule: Module = module {
    single<LoginRepository> { LoginRepositorylmpl(get(), get()) }
    single<BookSearchRepository> { BookSearchRepositorylmpl(get(),get()) }
    single<BookListRepository> { BookListRepositorylmpl(get(), get()) }
}

val localDataModule: Module = module {
    single { PreferenceManager(get()) }
    single<LoginLocalDataSource> { LoginLocalDataSourcelmpl(get()) }
    single<SearchBookLocalDataSource> { SearchBookLocalDataSourcelmpl(get(), get()) }
    single<RecentSearchesDao> { get<MyBookDatabase>().RecentSearchesDao() }
    single<BookListDao> { get<MyBookDatabase>().bookListDao() }
    single<TextMemoDao> { get<MyBookDatabase>().textMemoDao() }
    single<ImageMemoDao> { get<MyBookDatabase>().imageMemoDao() }
    single<MyBookDatabase> {
        Room.databaseBuilder(get(), MyBookDatabase::class.java, "MyBookDatabase").fallbackToDestructiveMigration().build()
    }
    single<BookListLocalDataSource> { BookListLocalDataSourcelmpl(get(), get()) }
}

val remoteDataModule: Module = module {
//    single { EmailLogin() }
    single { LoginManager(get()) }
    single<LoginRemoteDataSource> { LoginRemoteDataSourcelmpl(get(), get(), get()) }
    single<SearchBookRemoteDataSource> { SearchBookRemoteDataSourcelmpl(get()) }
    single<BookListRemoteDataSource> { BookListRemoteDataSourcelmpl(get()) }
}

val activityMoudel: Module = module {
    single { SignUpActivity() }
    single { LoginActivity() }
    single { SplashActivity() }
}

val fragmentModule: Module = module {
    single { BookListFragment() }
    single { BookSearchFragment() }
    single { MyBookHistoryFragment() }
    single { SettingFragment() }
}