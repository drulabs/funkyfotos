package com.appfest.funkyfotos.like;

import android.content.Context;

import com.appfest.funkyfotos.dto.Like;
import com.appfest.funkyfotos.firebase.LikesHandler;

import com.appfest.funkyfotos.R;

/**
 * Created by kaushald on 25/02/17.
 */

public class LikesPresenter implements LikesContract.Presenter, LikesHandler.Callback {

    private Context mContext;
    private LikesContract.View view;
    private String artifactId;
    private String artifactType;

    private LikesHandler likesHandler;

    public LikesPresenter(Context context, LikesContract.View view, String artifactId, String
            artifactType) {
        this.mContext = context;
        this.view = view;
        this.artifactId = artifactId;
        this.artifactType = artifactType;

        this.view.setPresenter(this);
        this.likesHandler = new LikesHandler(mContext, this, this.artifactId);
    }

    @Override
    public void fetchLikes() {
        view.showLoading();
        likesHandler.fetchLikes();
    }

    @Override
    public void start() {
        fetchLikes();
    }

    @Override
    public void destroy() {
        this.view = null;
        this.likesHandler = null;
    }

    @Override
    public void onLikeUpdated(boolean isLiked, boolean isSuccess) {

    }

    @Override
    public void onNoMoreLikes() {
        if (view != null) {
            view.onNoMoreLikes();
            view.hideLoading();
        }
    }

    @Override
    public void onLikeFetched(Like like) {
        if (view != null) {
            view.hideLoading();
            view.onLikeFetched(like);
        }
    }
}
