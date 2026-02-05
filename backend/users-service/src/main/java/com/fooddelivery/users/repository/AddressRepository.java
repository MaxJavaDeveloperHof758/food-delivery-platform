package com.fooddelivery.users.repository;

import com.fooddelivery.users.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
    List<Address> findByStreetContainingAndUserId (String street,Long userId);
    List<Address> findByCityContainingAndUserId (String city,Long userId);
    List<Address> findByStateContainingAndUserId (String state,Long userId);
    List<Address> findByCountryContainingAndUserId (String country,Long userId);

    List<Address> findByUserId(Long userId);
    List<Address> findByCityAndStreet(String city,String street);
    List<Address> findByCountryOrderByCityAsc(String country);
}
