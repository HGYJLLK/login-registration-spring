package com.example.login.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_song")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "singer_id", nullable = false)
    private Integer singerId;

    @Column(name = "singer_name", nullable = false, length = 100)
    private String singerName;

    @Column(name = "song_url", nullable = false, length = 500)
    private String songUrl;

    @Column(name = "pic_url", length = 500)
    private String picUrl;

    @Column(columnDefinition = "TEXT")
    private String lyric;

    @Column
    private Integer duration = 0;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    // Constructors
    public Song() {
        this.createTime = LocalDateTime.now();
    }

    public Song(String name, Integer singerId, String singerName, String songUrl) {
        this.name = name;
        this.singerId = singerId;
        this.singerName = singerName;
        this.songUrl = songUrl;
        this.createTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSingerId() {
        return singerId;
    }

    public void setSingerId(Integer singerId) {
        this.singerId = singerId;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
