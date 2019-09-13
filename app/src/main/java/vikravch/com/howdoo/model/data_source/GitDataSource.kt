package vikravch.com.howdoo.model.data_source

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import vikravch.com.howdoo.model.entities.Item
import vikravch.com.howdoo.model.entities.State
import vikravch.com.howdoo.model.network.GitSearchApi

class GitDataSource(
    private val query:String,
    private val gitSearchApi: GitSearchApi,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Item>() {

    var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Item>
    ) {
        updateState(State.LOADING)
        compositeDisposable.add(
            gitSearchApi.getSearchResult(query, 1, 30)
                .subscribe(
                    { response ->
                        updateState(State.DONE)
                        callback.onResult(response.items,
                             null,
                            2
                        )
                    },
                    {
                        updateState(State.ERROR)
                        setRetry(Action { loadInitial(params, callback) })
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Item>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            gitSearchApi.getSearchResult(query, params.key, params.requestedLoadSize)
                .subscribe(
                    { response ->
                        updateState(State.DONE)
                        callback.onResult(response.items,
                            params.key + 1
                        )
                    },
                    {
                        updateState(State.ERROR)
                        setRetry(Action { loadAfter(params, callback) })
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Item>) {}

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }
}