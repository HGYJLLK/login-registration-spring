package com.example.login.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_list_song")
public class ListSong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "song_list_id", nullable = false)
    private Integer songListId;

    @Column(name = "song_id", nullable = false)
    private Integer songId;

    // Constructors
    public ListSong() {
    }

    public ListSong(Integer songListId, Integer songId) {
        this.songListId = songListId;
        this.songId = songId;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSongListId() {
        return songListId;
    }

    public void setSongListId(Integer songListId) {
        this.songListId = songListId;
    }

    public Integer getSongId() {
        return songId;
    }

    public void setSongId(Integer songId) {
        this.songId = songId;
    }
}
