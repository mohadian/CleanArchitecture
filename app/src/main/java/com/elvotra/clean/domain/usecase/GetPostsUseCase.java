package com.elvotra.clean.domain.usecase;

import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.repository.IPostsRepository;
import com.elvotra.clean.domain.usecase.base.BaseUseCase;

import java.util.List;

public class GetPostsUseCase extends BaseUseCase<GetPostsUseCase.RequestValues, GetPostsUseCase.ResponseValue> {
    private IPostsRepository repository;

    public GetPostsUseCase(IPostsRepository repository) {
        this.repository = repository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        repository.getPosts(requestValues.isForceUpdate(), new IPostsRepository.LoadPostsCallback() {
            @Override
            public void onPostsLoaded(final List<Post> posts) {
                ResponseValue responseValue = new ResponseValue(posts);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onError(final int statusCode) {
                getUseCaseCallback().onError(statusCode);
            }
        });
    }

    public static final class RequestValues implements BaseUseCase.RequestValues {

        private final boolean mForceUpdate;

        public RequestValues(boolean forceUpdate) {
            mForceUpdate = forceUpdate;
        }

        public boolean isForceUpdate() {
            return mForceUpdate;
        }

    }

    public static final class ResponseValue implements BaseUseCase.ResponseValue {

        private final List<Post> posts;

        public ResponseValue(List<Post> posts) {
            this.posts = posts;
        }

        public List<Post> getPosts() {
            return posts;
        }
    }
}
