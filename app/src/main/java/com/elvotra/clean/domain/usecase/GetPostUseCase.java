package com.elvotra.clean.domain.usecase;

import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.usecase.base.UseCase;

public interface GetPostUseCase extends UseCase {

    interface Callback {
        void onPostRetrieved(Post post);

        void onRetrievalFailed(int statusCode);
    }
}
