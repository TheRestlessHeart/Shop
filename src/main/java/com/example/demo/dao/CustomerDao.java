package com.example.demo.dao;



import com.example.demo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by yancychan on 17-8-26.
 */
@Repository
public interface CustomerDao extends JpaRepository<Customer, String> {


//    void insert(Customer customer);
}
