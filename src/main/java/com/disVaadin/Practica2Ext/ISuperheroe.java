package com.disVaadin.Practica2Ext;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ISuperheroe extends JpaRepository<Superheroe, Long>{
    List<Superheroe> findByNombreStartsWithIgnoreCase(String value);
}
