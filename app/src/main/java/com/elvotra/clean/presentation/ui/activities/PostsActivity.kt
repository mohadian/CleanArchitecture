package com.elvotra.clean.presentation.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.elvotra.clean.R
import com.elvotra.clean.presentation.di.Injector
import com.elvotra.clean.presentation.presenter.PostsPresenter
import com.elvotra.clean.presentation.ui.fragments.PostsListFragment
import com.elvotra.clean.utils.ActivityUtils

class PostsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        var postsListFragment: PostsListFragment? = supportFragmentManager.findFragmentById(R.id.contentFrame) as PostsListFragment

        if (postsListFragment == null) {
            // Create the fragment
            postsListFragment = PostsListFragment.newInstance()
            ActivityUtils.addFragmentToActivity(
                    supportFragmentManager, postsListFragment, R.id.contentFrame)
        }

        PostsPresenter(postsListFragment, Injector.provideGetPostsUseCase(this), Injector.provideUseCaseHandler())
    }
}
