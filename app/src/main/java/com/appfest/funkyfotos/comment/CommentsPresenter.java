package com.appfest.funkyfotos.comment;

import android.app.Activity;

import com.appfest.funkyfotos.dto.Comment;

import com.appfest.funkyfotos.R;

import com.appfest.funkyfotos.firebase.CommentsHandler;
import com.appfest.funkyfotos.ui.NotificationToast;

import java.util.List;

/**
 * Created by kaushald on 10/02/17.
 */

public class CommentsPresenter implements CommentContract.Presenter, CommentsHandler.Callback {

    private Activity activity;
    private CommentContract.View view;
    private String artifactId;
    private String artifactType;

    private CommentsHandler commentsHandler;

    public CommentsPresenter(Activity activity, CommentContract.View view, String artifactId,
                             String artifactType) {
        this.activity = activity;
        this.view = view;
        this.artifactId = artifactId;
        this.artifactType = artifactType;
        this.view.setPresenter(this);

        this.commentsHandler = new CommentsHandler(this.activity, this, this.artifactId, this
                .artifactType);
    }

    @Override
    public void loadNextBatch() {
        commentsHandler.fetchComments();
    }

    @Override
    public void addComment(Comment comment) {
        commentsHandler.addComment(comment);
        if (view != null) {
            view.showLoading();
        }
    }

    @Override
    public void start() {
        loadNextBatch();
    }

    @Override
    public void destroy() {
        if (view != null) {
            this.view = null;
            this.commentsHandler = null;
        }
    }

    @Override
    public void onCommentAddedSuccessfully() {
        NotificationToast.showToast(activity, activity.getString(R.string
                .comment_added_successfully));
        if (view != null) {
            view.hideLoading();
            view.onCommentSaved();
        }
    }

    @Override
    public void onCommentsFetched(List<Comment> comments) {
        if (view != null) {
            view.loadComments(comments);
        }
    }

    @Override
    public void onCommentFetched(Comment comment) {
        if (view != null) {
            view.loadComment(comment);
        }
    }

    @Override
    public void onNoCommentsFound() {
        if (view != null) {
            view.hideLoading();
        }
    }

    @Override
    public void onError() {
        if (view != null) {
            view.onLoadError(activity.getString(R.string.no_comments_found));
            view.hideLoading();
        }
    }
}
