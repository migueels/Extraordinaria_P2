package com.disVaadin.Practica2Ext;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IHabilidades extends  JpaRepository<Habilidad,Long>{

    List<Habilidad> findByidSuperheroe(Long i);
}
