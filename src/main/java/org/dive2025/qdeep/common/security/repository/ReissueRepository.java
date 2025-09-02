package org.dive2025.qdeep.common.security.repository;

import org.dive2025.qdeep.common.security.entity.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReissueRepository extends JpaRepository<Refresh,Long> {

    Optional<Refresh> deleteByRefresh(String refresh);




}
