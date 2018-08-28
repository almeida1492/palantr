package com.test.android.palantr.Entities;

/**
 * Created by henriquedealmeida on 26/04/18.
 */

public class Post {

    private String id_post;
    private int creator;
    private String body;
    private String media;
    private String signature;
    private String topic;
    private Long votes;
    private String date;

    public Post() {}

    public Post(String id_post, int creator, String body, String media, String signature, String topic,
                Long votes, String date) {
        this.id_post = id_post;
        this.creator = creator;
        this.body = body;
        this.media = media;
        this.signature = signature;
        this.topic = topic;
        this.votes = votes;
        this.date = date;
    }

    public String getId_post() {
        return id_post;
    }

    public void setId_post(String id_post) {
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

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
