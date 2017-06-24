package com.appfest.funkyfotos.like;

import com.appfest.funkyfotos.BasePresenter;
import com.appfest.funkyfotos.BaseView;
import com.appfest.funkyfotos.dto.Like;

/**
 * Created by kaushald on 25/02/17.
 */

public interface LikesContract {

    interface View extends BaseView<Presenter> {
        void showLoading();
        void hideLoading();
        void onLikeFetched(Like like);
        void onNoMoreLikes();
    }

    interface Presenter extends BasePresenter {
        void fetchLikes();
    }

}
