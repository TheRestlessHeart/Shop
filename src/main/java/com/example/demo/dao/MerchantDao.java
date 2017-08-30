package com.example.demo.dao;



import com.example.demo.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by yancychan on 17-8-26.
 */
@Repository
public interface MerchantDao extends JpaRepository<Merchant, String> {

    @Query("from Merchant where merchant_id = :merchant_id")
    Merchant findById(@Param("merchant_id") String merchant_id);
}
