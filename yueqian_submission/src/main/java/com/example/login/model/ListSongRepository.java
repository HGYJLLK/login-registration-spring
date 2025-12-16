package com.example.login.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListSongRepository extends JpaRepository<ListSong, Integer> {
    List<ListSong> findBySongListId(Integer songListId);
    void deleteBySongListId(Integer songListId);
    void deleteBySongListIdAndSongId(Integer songListId, Integer songId);
}
