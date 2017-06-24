package com.appfest.funkyfotos;

import android.app.Activity;

import com.appfest.funkyfotos.comment.CommentsPresenter;
import com.appfest.funkyfotos.login.LoginPresenter;
import com.appfest.funkyfotos.login.ReqAccessPresenter;
import com.appfest.funkyfotos.add.AddPicContract;
import com.appfest.funkyfotos.add.AddPicPresenter;
import com.appfest.funkyfotos.comment.CommentContract;
import com.appfest.funkyfotos.landing.PicsContract;
import com.appfest.funkyfotos.landing.PicsPresenter;
import com.appfest.funkyfotos.like.LikesContract;
import com.appfest.funkyfotos.like.LikesPresenter;
import com.appfest.funkyfotos.login.LoginContract;
import com.appfest.funkyfotos.login.ReqAccessContract;
import com.appfest.funkyfotos.poem.PoemContract;
import com.appfest.funkyfotos.poem.PoemsPresenter;

/**
 * Created by kaushald on 25/01/17.
 */

public class PresenterCreator {

    public static LoginContract.Presenter createLoginPresenter(Activity context, LoginContract.View
            view) {
        return new LoginPresenter(context, view);
    }

    public static ReqAccessContract.Presenter createReqAccessPresenter(Activity context,
                                                                       ReqAccessContract.View view) {
        return new ReqAccessPresenter(context, view);
    }

    public static PicsContract.Presenter createPicsPresenter(Activity activity, PicsContract.View
            view) {
        return new PicsPresenter(activity, view);
    }

    public static AddPicContract.Presenter createAddPicPresenter(Activity activity,
                                                                 AddPicContract.View view) {
        return new AddPicPresenter(activity, view);
    }

    public static CommentContract.Presenter createCommentsPresenter(Activity activity, CommentContract
            .View view, String artifactId, String artifactType) {
        return new CommentsPresenter(activity, view, artifactId, artifactType);
    }

    public static PoemContract.Presenter createPoemsPresenter(Activity activity, PoemContract
            .View view) {
        return new PoemsPresenter(activity, view);
    }

    public static LikesContract.Presenter createLikesPresenter(Activity activity, LikesContract
            .View view, String artifactId, String artifactType) {
        return new LikesPresenter(activity, view, artifactId, artifactType);
    }
}
