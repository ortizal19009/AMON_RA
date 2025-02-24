package com.itzel.repositorio.contabilidad;

import com.itzel.modelo.contabilidad.Presupuesto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PresupuestoR extends JpaRepository<Presupuesto, Long> {
    @Query(value = "SELECT * FROM presupuesto p WHERE p.tippar = ?1 ORDER BY p.codpar",nativeQuery = true)
    public Page<Presupuesto> findByTipparPageable(int tippar, Pageable pageable);
    @Query(value = "SELECT * FROM presupuesto p WHERE (p.codpar like %?1% or LOWER(p.nompar) like %?1%) and p.tippar = ?2 ORDER BY p.codpar",nativeQuery = true)
    public Page<Presupuesto> findByParDenom(String dato, int tippar, Pageable pageable);
    @Query(value = "SELECT * FROM presupuesto p WHERE p.codpar = ?1 ORDER BY p.codpar",nativeQuery = true)
    public Presupuesto findByCodpar(String codpar);
}
