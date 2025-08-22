package org.dive2025.qdeep.domain.store.repository;

import org.dive2025.qdeep.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store,Long> {

    boolean existsByNameAndAddress(String name,String address);
}
