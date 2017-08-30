package com.example.demo.dao;

import com.example.demo.entity.Express;
import com.example.demo.entity.Good;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.tools.JavaCompiler;

/**
 * Created by yancychan on 17-8-30.
 */
@Repository
public interface ExpressDao extends JpaRepository<Express, String> {

    @Query("from Express where express_type = :send_type")
    Express findByType(@Param("send_type") String send_type);

    @Query("from Express where express_id = :send_id")
    Express findById(@Param("send_id") String send_id);
}
