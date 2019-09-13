package vikravch.com.howdoo.model.data_source

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.reactivex.disposables.CompositeDisposable
import vikravch.com.howdoo.model.entities.Item
import vikravch.com.howdoo.model.network.GitSearchApi

class GitDataSourceFactory(
    private val query: String,
    private val compositeDisposable: CompositeDisposable,
    private val gitSearchApi: GitSearchApi
) : DataSource.Factory<Int, Item>() {

    val newsDataSourceLiveData = MutableLiveData<GitDataSource>()

    override fun create(): DataSource<Int, Item> {
        val newsDataSource = GitDataSource(query, gitSearchApi, compositeDisposable)
        newsDataSourceLiveData.postValue(newsDataSource)
        return newsDataSource
    }
}