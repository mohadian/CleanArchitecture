package com.elvotra.clean.domain.usecase;

import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.usecase.base.UseCase;

import java.util.List;

public interface GetPostsUseCase extends UseCase {

    interface Callback {
        void onPostsRetrieved(List<Post> posts);

        void onRetrievalFailed(int statusCode);
    }
}
