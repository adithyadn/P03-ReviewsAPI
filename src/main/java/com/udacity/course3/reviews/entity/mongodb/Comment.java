package com.udacity.course3.reviews.entity.mongodb;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Comment {

    private Integer commentID;
    private String commentText;

    public Integer getCommentID() {
        return commentID;
    }

    public void setCommentID(Integer commentID) {
        this.commentID = commentID;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

}
