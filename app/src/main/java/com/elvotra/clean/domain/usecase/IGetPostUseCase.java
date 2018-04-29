package com.elvotra.clean.domain.usecase;

import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.usecase.base.IUseCase;

public interface IGetPostUseCase extends IUseCase {

    interface Callback {
        void onPostRetrieved(Post post);

        void onRetrievalFailed(int statusCode);
    }
}
