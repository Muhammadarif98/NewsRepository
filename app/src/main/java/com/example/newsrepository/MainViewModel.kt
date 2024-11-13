package com.example.newsrepository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsrepository.data.api.NewsRepository
import com.example.newsrepository.models.NewsResponse
import com.example.newsrepository.utils.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale.IsoCountryCode
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: NewsRepository):
    ViewModel() {
        val newsLiveData:MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    var newsPage = 1
    init {
        getNews("kz")
    }
    private fun getNews(countryCode: String) =
        viewModelScope.launch {
            newsLiveData.postValue(Resources.Loading())
            val response = repository.getNews(countryCode = countryCode, pageNumber = newsPage)
            if (response.isSuccessful) {
                response.body()?.let { newsResponse ->
                    newsLiveData.postValue(Resources.Success(newsResponse))
                }
            } else {
                newsLiveData.postValue(Resources.Error(response.message()))
            }
        }
}