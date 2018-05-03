package com.elvotra.clean.domain.usecase;

import com.elvotra.clean.domain.model.Post;
import com.elvotra.clean.domain.repository.IPostsRepository;
import com.elvotra.clean.domain.usecase.base.BaseUseCase;

public class GetPostUseCase extends BaseUseCase<GetPostUseCase.RequestValues, GetPostUseCase.ResponseValue> {
    private IPostsRepository repository;

    public GetPostUseCase(IPostsRepository repository) {
        this.repository = repository;
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        int postId = requestValues.getPostId();
        repository.getPost(postId, new IPostsRepository.LoadPostCallback() {
            @Override
            public void onPostLoaded(Post post) {
                GetPostUseCase.ResponseValue responseValue = new GetPostUseCase.ResponseValue(post);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onError(final int statusCode) {
                getUseCaseCallback().onError(statusCode);
            }
        });
    }

    public static final class RequestValues implements BaseUseCase.RequestValues {

        private final int postId;

        public RequestValues(int postId) {
            this.postId = postId;
        }

        public int getPostId() {
            return postId;
        }
    }

    public static final class ResponseValue implements BaseUseCase.ResponseValue {

        private Post post;

        public ResponseValue(Post post) {
            this.post = post;
        }

        public Post getPost() {
            return this.post;
        }
    }
}
