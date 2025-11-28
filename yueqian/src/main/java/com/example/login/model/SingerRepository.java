package com.example.login.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SingerRepository extends JpaRepository<Singer, Integer> {

    // 根据名称查找歌手
    List<Singer> findByNameContaining(String name);

    // 根据名称精确查找
    Singer findByName(String name);
}
