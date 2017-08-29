package com.example.demo.dao;

import com.example.demo.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yancychan on 17-8-26.
 */
@Repository
public interface OrdersDao extends JpaRepository<Orders, String> {

    @Query("from Orders where id = :ordersId")
    Orders findById(@Param("ordersId") String ordersId);

    @Query("from Orders where good_id = :goodId")
    List<Orders> findByGoodId(@Param("goodId") String goodId);
}

