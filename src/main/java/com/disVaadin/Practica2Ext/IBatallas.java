package com.disVaadin.Practica2Ext;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IBatallas extends JpaRepository<Batalla,Long>{
    List<Batalla> findByidSuperheroe(Long i);


}
