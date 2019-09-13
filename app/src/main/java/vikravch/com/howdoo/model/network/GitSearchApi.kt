package vikravch.com.howdoo.model.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import vikravch.com.howdoo.model.entities.GitResponse

interface GitSearchApi {
    @GET("/search/repositories")
    fun getSearchResult(
                        @Query("q") q:String?,
                        @Query("page") page:Int,
                        @Query("per_page") perPage:Int ): Observable<GitResponse>
}