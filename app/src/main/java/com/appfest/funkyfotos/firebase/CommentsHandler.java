package com.appfest.funkyfotos.firebase;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.appfest.funkyfotos.config.Constants;
import com.appfest.funkyfotos.dto.Comment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by kaushald on 10/02/17.
 */

public class CommentsHandler {

    private static final int BATCH_SIZE = 10;
    private static final String TAG = CommentsHandler.class.getSimpleName();

    private long lastTimestamp = System.currentTimeMillis();
    private boolean hasMoreComments = true;

    private Activity activity;
    private Callback callback;
    private String artifactId;

    // Firebase variables
    private DatabaseReference commentsDB;
    private DatabaseReference artifactDB;
    private ChildEventListener commentChildListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot != null && dataSnapshot.hasChildren()) {
                Comment singleComment = dataSnapshot.getValue(Comment.class);
                singleComment.setCommentId(dataSnapshot.getKey());
                callback.onCommentFetched(singleComment);
            } else {
                callback.onError();
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Log.d(TAG, "onChildChanged() called with: dataSnapshot = [" + dataSnapshot + "], s = [" + s + "]");
            if (dataSnapshot != null && dataSnapshot.hasChildren()) {
                Comment modifiedComment = dataSnapshot.getValue(Comment.class);
                modifiedComment.setCommentId(dataSnapshot.getKey());
                callback.onCommentFetched(modifiedComment);
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public CommentsHandler(Activity activity, Callback callback, String artifactId, String artifactType) {
        this.activity = activity;
        this.callback = callback;
        this.artifactId = artifactId;

        commentsDB = FirebaseDatabase.getInstance().getReference().child(Constants
                .COMMENTS_DB);
        artifactDB = FirebaseDatabase.getInstance().getReference().child(artifactType).child
                (artifactId);
    }

    public void fetchComments() {

        if (hasMoreComments) {
            Query commentsQuery = commentsDB.orderByChild(Constants.COMMENTS_ARTIFACT_ID).equalTo
                    (artifactId);
            commentsQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.hasChildren()) {
                        //Comment singleComment = dataSnapshot.getValue(Comment.class);
                        //callback.onCommentFetched(singleComment);
                    } else {
                        callback.onError();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //commentsQuery.addListenerForSingleValueEvent(commentsListener);
            commentsQuery.addChildEventListener(commentChildListener);
        }
    }

//    private ValueEventListener commentsListener = new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            if (dataSnapshot != null && dataSnapshot.hasChildren()) {
//
//                List<Comment> comments = new ArrayList<>();
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Comment singleComment = snapshot.getValue(Comment.class);
//                    comments.add(singleComment);
//                }
//
//                Collections.sort(comments);
//
//                callback.onCommentsFetched(comments);
//
//            } else {
//                callback.onError();
//            }
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//            callback.onError();
//        }
//    };

    public void addComment(Comment comment) {
        comment.setArtifactId(artifactId);
        commentsDB.push().setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                artifactDB.child(Constants.KEY_COMMENTS_COUNT)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null) {
                                    int commentsCount = dataSnapshot.getValue(Integer.class);

                                    // adding comment count
                                    artifactDB.child(Constants.KEY_COMMENTS_COUNT).setValue
                                            (commentsCount + 1);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                callback.onCommentAddedSuccessfully();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onError();
            }
        });
    }

    public interface Callback {
        //TODO implement pagination here
        void onCommentsFetched(List<Comment> comments);

        void onCommentFetched(Comment comment);

        void onCommentAddedSuccessfully();

        void onNoCommentsFound();

        void onError();
    }
}
