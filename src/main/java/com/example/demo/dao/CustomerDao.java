package com.example.demo.dao;



import com.example.demo.entity.Customer;
import com.example.demo.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by yancychan on 17-8-26.
 */
@Repository
public interface CustomerDao extends JpaRepository<Customer, String> {

    @Query("from Customer where customer_name = :customerName ")
    Customer findByCustomerName(@Param("customerName") String customerName);

    @Query("from Customer where token = :token")
    Customer findByToken(@Param("token") String token);
}
