package se.iwoio.newssweden.network

import se.iwoio.newssweden.data.NewsResult
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET("/v2/top-headlines")
    suspend fun getNews(@Query("country") country: String,
                              @Query("apiKey") apiKey: String): NewsResult

    @GET("/v2/top-headlines")
    suspend fun getNewsAsString(@Query("country") country: String,
                                @Query("apiKey") apiKey: String): String

}
