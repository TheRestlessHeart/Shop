package com.example.demo.dao;

import com.example.demo.entity.Good;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yancychan on 17-8-26.
 */
@Repository
public interface GoodDao extends JpaRepository<Good, String> {

    @Query("from Good where good_id = :goodId")
    Good findById(@Param("goodId") String goodId);

    @Query("from Good where good_type = :good_type")
    List<Good> findByType(@Param("good_type") String good_type);
}
