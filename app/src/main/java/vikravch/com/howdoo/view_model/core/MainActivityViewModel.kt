package vikravch.com.howdoo.view_model.core

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.vikravch.poloniexproject.view_model.core.BaseViewModel
import io.reactivex.disposables.CompositeDisposable
import vikravch.com.howdoo.model.network.GitSearchApi
import javax.inject.Inject

class MainActivityViewModel : BaseViewModel() {
    @Inject
    lateinit var gitApi: GitSearchApi
    val query: MutableLiveData<String> = MutableLiveData()

    val compositeDisposable = CompositeDisposable()
    val pageSize = 30

    val config = PagedList.Config.Builder()
        .setPageSize(pageSize)
        .setInitialLoadSizeHint(pageSize)
        .setEnablePlaceholders(false)
        .build()
}