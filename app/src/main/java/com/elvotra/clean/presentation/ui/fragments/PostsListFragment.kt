package com.elvotra.clean.presentation.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.elvotra.clean.R
import com.elvotra.clean.presentation.contract.PostsContract
import com.elvotra.clean.presentation.model.PostViewItem
import com.elvotra.clean.presentation.ui.activities.PostDetailsActivity
import com.elvotra.clean.presentation.ui.activities.PostDetailsActivity.Companion.POST_ID
import com.elvotra.clean.presentation.ui.adapters.PostsRecyclerAdapter
import com.elvotra.clean.presentation.ui.widgets.ScrollChildSwipeRefreshLayout

class PostsListFragment : Fragment(), PostsContract.View {

    override lateinit var presenter: PostsContract.IPostsPresenter

    private lateinit var postsRecyclerAdapter: PostsRecyclerAdapter

    private lateinit var fragment_posts_list_recycler_view: RecyclerView
    private lateinit var fragment_posts_list_message: TextView
    private lateinit var refresh_layout: ScrollChildSwipeRefreshLayout

    override val isActive: Boolean
        get() = isAdded

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_posts_list, container, false)

        val mLayoutManager = LinearLayoutManager(activity)
        fragment_posts_list_recycler_view.layoutManager = mLayoutManager

        refresh_layout.setColorSchemeColors(
                ContextCompat.getColor(activity!!, R.color.colorPrimary),
                ContextCompat.getColor(activity!!, R.color.colorAccent),
                ContextCompat.getColor(activity!!, R.color.colorPrimaryDark)
        )
        refresh_layout.setScrollUpChild(fragment_posts_list_recycler_view)

        refresh_layout.setOnRefreshListener { presenter.loadPosts(true) }
        presenter.loadPosts(true)

        return rootView

    }

    fun getPresenter(presenter: PostsContract.IPostsPresenter): PostsContract.IPostsPresenter? {
        return this.presenter
    }

    override fun showPostsList(postViewItems: List<PostViewItem>) {
        fragment_posts_list_message.visibility = View.GONE

        fragment_posts_list_recycler_view.visibility = View.VISIBLE

        postsRecyclerAdapter = PostsRecyclerAdapter(context!!, postViewItems, PostsRecyclerAdapter.PostsListItemClickListener { override fun onPostClicked(postId: Int){
            presenter.openPostDetails(postId)
            }
        })

        fragment_posts_list_recycler_view.adapter = postsRecyclerAdapter
        //{ postId: Int -> presenter.openPostDetails(postId) }
    }

    override fun showPostDetails(postId: Int) {
        val i = Intent(activity, PostDetailsActivity::class.java)
        i.putExtra(POST_ID, postId)
        startActivity(i)
    }

    override fun showNoResults() {
        fragment_posts_list_recycler_view.visibility = View.GONE
        fragment_posts_list_message.visibility = View.VISIBLE
        fragment_posts_list_message.text = getString(R.string.no_posts)
    }

    override fun showProgress() {
        refresh_layout.isRefreshing = true
    }

    override fun hideProgress() {
        refresh_layout.isRefreshing = false
    }

    override fun showError(message: String) {
        fragment_posts_list_recycler_view!!.visibility = View.GONE
        fragment_posts_list_message.visibility = View.VISIBLE
        fragment_posts_list_message.text = message
    }

    companion object {

        fun newInstance(): PostsListFragment {
            return PostsListFragment()
        }
    }
}
