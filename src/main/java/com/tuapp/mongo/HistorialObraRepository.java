package com.tuapp.mongo;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.tuapp.dto.CambioPorAccionDto;

// Repositorio Mongo para consultar historial de cambios de obras.
public interface HistorialObraRepository extends MongoRepository<HistorialObra, String> {

    List<HistorialObra> findByObraIdOrderByTimestampDesc(Long obraId);

    List<HistorialObra> findByUserIdOrderByTimestampDesc(Long userId);

    List<HistorialObra> findByTimestampBetweenOrderByTimestampDesc(Instant inicio, Instant fin);

    @Aggregation(pipeline = {
        "{ '$group': { '_id': '$accion', 'total': { '$sum': 1 } } }",
        "{ '$project': { '_id': 0, 'accion': '$_id', 'total': 1 } }",
        "{ '$sort': { 'total': -1 } }"
    })
    List<CambioPorAccionDto> contarCambiosPorAccion();
}
