package vikravch.com.howdoo.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_footer.view.*
import vikravch.com.howdoo.R
import vikravch.com.howdoo.databinding.ItemPostBinding
import vikravch.com.howdoo.model.entities.Item
import vikravch.com.howdoo.model.entities.State
import vikravch.com.howdoo.view_model.core.ItemViewModel

class PostListAdapter(private val retry: () -> Unit, private val query: String) :
    PagedListAdapter<Item, RecyclerView.ViewHolder>(NewsDiffCallback) {

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) {
            ViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_post,
                    parent,
                    false
                )
            )
        } else {
            ListFooterViewHolder.create(retry, parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as ViewHolder).bind(getItem(position)!!, query)
        else (holder as ListFooterViewHolder).bind(state)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    companion object {
        val NewsDiffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.fullName == newItem.fullName
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == State.LOADING || state == State.ERROR)
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }

    class ViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = ItemViewModel()

        fun bind(post: Item, query: String) {
            viewModel.bind(post, query)
            binding.viewModel = viewModel
        }
    }


    class ListFooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(status: State?) {
            itemView.progress_bar.visibility =
                if (status == State.LOADING) VISIBLE else View.INVISIBLE
            itemView.txt_error.visibility = if (status == State.ERROR) VISIBLE else View.INVISIBLE
        }

        companion object {
            fun create(retry: () -> Unit, parent: ViewGroup): ListFooterViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_footer, parent, false)
                view.txt_error.setOnClickListener { retry() }
                return ListFooterViewHolder(view)
            }
        }
    }
}