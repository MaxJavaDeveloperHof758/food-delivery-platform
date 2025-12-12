package com.fooddelivery.users.repository;

import com.fooddelivery.users.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
    List<Address> findAllByStreet (String street);
    List<Address> findAllByCity (String city);
    List<Address> findAllByState (String state);
    List<Address> findAllByCountry (String country);

    List<Address> findByUserId(Long userId);
    List<Address> findByCityAndStreet(String city,String street);
    List<Address> findByCountryOrderByCityAsc(String country);
}
