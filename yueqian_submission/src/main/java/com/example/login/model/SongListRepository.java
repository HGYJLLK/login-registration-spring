package com.example.login.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongListRepository extends JpaRepository<SongList, Integer> {
    List<SongList> findByTitleContaining(String title);
    List<SongList> findByStyle(String style);
}
