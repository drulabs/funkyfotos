package com.appfest.funkyfotos.dto;

/**
 * Created by kaushald on 10/02/17.
 */

public class Comment implements Comparable<Comment> {

    private String artifactId;
    private String commenter;
    private String commenterPic;
    private String commenterId;
    private String text;
    private long timestamp;
    private String commentId;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getCommenter() {
        return commenter;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCommenterPic() {
        return commenterPic;
    }

    public void setCommenterPic(String commenterPic) {
        this.commenterPic = commenterPic;
    }

    public String getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(String commenterId) {
        this.commenterId = commenterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        return commentId != null?commentId.equals(comment.commentId):false;

    }

    @Override
    public int hashCode() {
        int result = artifactId.hashCode();
        result = 31 * result + commenter.hashCode();
        result = 31 * result + (commenterPic != null ? commenterPic.hashCode() : 0);
        result = 31 * result + text.hashCode();
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    @Override
    public int compareTo(Comment comment) {
        if (this.timestamp > comment.getTimestamp()) {
            return -1;
        } else if (this.timestamp < comment.getTimestamp()) {
            return 1;
        } else {
            return 0;
        }
    }
}
