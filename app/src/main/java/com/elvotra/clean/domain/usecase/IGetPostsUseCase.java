package com.elvotra.clean.domain.usecase;

import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.usecase.base.IUseCase;

import java.util.List;

public interface IGetPostsUseCase extends IUseCase {

    interface Callback {
        void onPostsRetrieved(List<Post> posts);

        void onRetrievalFailed(int statusCode);
    }
}
