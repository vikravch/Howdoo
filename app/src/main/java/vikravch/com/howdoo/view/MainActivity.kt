package vikravch.com.howdoo.view

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.LivePagedListBuilder
import kotlinx.android.synthetic.main.activity_main.*
import vikravch.com.howdoo.R
import vikravch.com.howdoo.databinding.ActivityMainBinding
import vikravch.com.howdoo.model.data_source.GitDataSourceFactory
import vikravch.com.howdoo.view_model.core.MainActivityViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        viewModel.query.observe(this, Observer { text ->
            post_list.visibility = GONE
            initAdapter(text)
        })

        binding.viewModel = viewModel
    }

    private fun initAdapter(text: String) {
        val gitDataSourceFactory =
            GitDataSourceFactory(text, viewModel.compositeDisposable, viewModel.gitApi)
        val postListAdapter =
            PostListAdapter({ gitDataSourceFactory.newsDataSourceLiveData.value?.retry() }, text)
        val newsList = LivePagedListBuilder(gitDataSourceFactory, viewModel.config).build()
        newsList.observe(this, Observer {
            Log.d("tag", "updated! ${it.size}")
            postListAdapter.submitList(it)
        })
        post_list.adapter = postListAdapter
        post_list.visibility = VISIBLE
    }
}
