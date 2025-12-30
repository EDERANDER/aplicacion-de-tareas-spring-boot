package com.alura.apptareas.repository;

import com.alura.apptareas.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    @Query("SELECT t FROM Tarea t WHERE t.recordatorio >= :inicio AND t.recordatorio < :fin AND t.notificado = false")
    List<Tarea> findTareasPendientesParaNotificar(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    List<Tarea> findByUsuarioIdOrderByIdDesc(Long idUsuario);

    void deleteAllByUsuarioId(Long usuarioId);
}
