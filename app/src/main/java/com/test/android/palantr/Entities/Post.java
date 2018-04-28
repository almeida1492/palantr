package com.test.android.palantr.Entities;

import android.graphics.Bitmap;

/**
 * Created by henriquedealmeida on 26/04/18.
 */

public class Post {

    int id_post;
    int creator;
    String body;
    Bitmap media;
    String signature;
    String topic;
    Long votes;
    int dates;

    public Post() {}

    public Post(int id_post, int creator, String body, Bitmap media, String signature, String topic,
                Long votes, int dates) {
        this.id_post = id_post;
        this.creator = creator;
        this.body = body;
        this.media = media;
        this.signature = signature;
        this.topic = topic;
        this.votes = votes;
        this.dates = dates;
    }

    public int getId_post() {
        return id_post;
    }

    public void setId_post(int id_post) {
        this.id_post = id_post;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Bitmap getMedia() {
        return media;
    }

    public void setMedia(Bitmap media) {
        this.media = media;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Long getVotes() {
        return votes;
    }

    public void setVotes(Long votes) {
        this.votes = votes;
    }

    public int getDates() {
        return dates;
    }

    public void setDates(int dates) {
        this.dates = dates;
    }
}
