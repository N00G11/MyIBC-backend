package com.app.MyIBC.GestionDesCamps.repository;


import com.app.MyIBC.GestionDesCamps.entity.Camp;
import com.app.MyIBC.GestionDesCamps.utils.TypeCamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CampRepository extends JpaRepository<Camp, Long> {
    Optional<Camp> findCampByType(String type);
    void deleteCampByType(TypeCamp type);
}
