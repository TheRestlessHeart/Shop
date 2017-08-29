package com.example.demo.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by yancychan on 17-8-26.
 */
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "comment_id", columnDefinition = "varchar(64) binary")
    private String comment_id;

    @Column(name = "comment_date")
    private String comment_date;

    @Column(name = "comment_title")
    private String comment_title;

    @Column(name = "comment_content")
    private String comment_content;

    @Column(name = "comment_score")
    private String comment_score;

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_date() {
        return comment_date;
    }

    public void setComment_date(String comment_date) {
        this.comment_date = comment_date;
    }

    public String getComment_title() {
        return comment_title;
    }

    public void setComment_title(String comment_title) {
        this.comment_title = comment_title;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getComment_score() {
        return comment_score;
    }

    public void setComment_score(String comment_score) {
        this.comment_score = comment_score;
    }
}
