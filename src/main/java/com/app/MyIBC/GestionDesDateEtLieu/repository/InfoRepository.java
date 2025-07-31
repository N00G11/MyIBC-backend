package com.app.MyIBC.GestionDesDateEtLieu.repository;


import com.app.MyIBC.GestionDesDateEtLieu.entity.Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoRepository extends JpaRepository<Info, Long> {
}
