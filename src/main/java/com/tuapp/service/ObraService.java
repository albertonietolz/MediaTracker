package com.tuapp.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tuapp.domain.Estado;
import com.tuapp.domain.Obra;
import com.tuapp.domain.Plataforma;
import com.tuapp.domain.TipoObra;
import com.tuapp.domain.Usuario;
import com.tuapp.dto.ObraDto;
import com.tuapp.mongo.Evento;
import com.tuapp.mongo.EventoRepository;
import com.tuapp.repository.ObraRepository;
import com.tuapp.repository.PlataformaRepository;
import com.tuapp.repository.UsuarioRepository;

// Servicio de negocio para gestionar obras y registrar eventos de auditoría asociados.
@Service
public class ObraService {

    // =========================
    // ZONA 1: DEPENDENCIAS
    // =========================
    private final ObraRepository obraRepository;
    private final PlataformaRepository plataformaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EventoRepository eventoRepository;
    private final HistorialObraService historialObraService;

    // =========================
    // ZONA 2: CONSTRUCTOR
    // =========================
    public ObraService(ObraRepository obraRepository,
            PlataformaRepository plataformaRepository,
            UsuarioRepository usuarioRepository,
            EventoRepository eventoRepository,
            HistorialObraService historialObraService) {
        this.obraRepository = obraRepository;
        this.plataformaRepository = plataformaRepository;
        this.usuarioRepository = usuarioRepository;
        this.eventoRepository = eventoRepository;
        this.historialObraService = historialObraService;
    }

    // =========================
    // ZONA 3: OPERACIONES DE NEGOCIO
    // =========================
    public Obra crearObra(ObraDto dto) {
        Obra obra = new Obra();
        aplicarDtoAEntidad(dto, obra);
        Obra guardada = obraRepository.save(obra);
        registrarEvento("CREATE_OBRA", guardada);
        return guardada;
    }

    public Obra actualizarObra(Long id, ObraDto dto) {
        Obra obra = obraRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Obra no encontrada"));

        Map<String, Object> antes = snapshotObra(obra);
        aplicarDtoAEntidad(dto, obra);
        Obra guardada = obraRepository.save(obra);

        historialObraService.registrarCambio(
                guardada.getId(),
                guardada.getCreador() != null ? guardada.getCreador().getId() : null,
                "UPDATE_OBRA",
                antes,
                snapshotObra(guardada));

        registrarEvento("UPDATE_OBRA", guardada);
        return guardada;
    }

    public Page<Obra> listarObras(TipoObra tipo, Estado estado, Long usuarioId, Pageable pageable) {
        if (tipo == null && estado == null && usuarioId == null) {
            return obraRepository.findAll(pageable);
        } else if (tipo != null && estado == null && usuarioId == null) {
            return obraRepository.findByTipo(tipo, pageable);
        } else if (tipo == null && estado != null && usuarioId == null) {
            return obraRepository.findByEstado(estado, pageable);
        } else if (tipo != null && estado != null && usuarioId == null) {
            return obraRepository.findByTipoAndEstado(tipo, estado, pageable);
        } else if (tipo == null && estado == null) {
            return obraRepository.findByCreadorId(usuarioId, pageable);
        } else if (tipo != null && estado == null) {
            return obraRepository.findByTipoAndCreadorId(tipo, usuarioId, pageable);
        } else if (tipo == null) {
            return obraRepository.findByEstadoAndCreadorId(estado, usuarioId, pageable);
        } else {
            return obraRepository.findByTipoAndEstadoAndCreadorId(tipo, estado, usuarioId, pageable);
        }
    }

    public Page<Obra> listarPorTipoPaginado(TipoObra tipo, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return obraRepository.findByTipo(tipo, pageable);
    }

    public List<Obra> listarPorEstado(Estado estado) {
        return obraRepository.findByEstado(estado);
    }

    public Double obtenerMediaNotaPorTipo(TipoObra tipo) {
        return obraRepository.mediaNotaPorTipo(tipo);
    }

    public Obra obtenerPorId(Long id) {
        return obraRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Obra no encontrada"));
    }

    public List<Obra> listarObrasPorUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new NoSuchElementException("Usuario no encontrado");
        }
        return obraRepository.findByCreadorId(usuarioId, Pageable.unpaged()).getContent();
    }

    public List<Obra> listarObrasConResenas() {
        return obraRepository.findObrasConResenas();
    }

    public void eliminarObra(Long id) {
        Obra obra = obraRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Obra no encontrada"));

        registrarEventoDelete(obra);
        obraRepository.delete(obra);
    }

    // =========================
    // ZONA 4: MAPEOS Y VALIDACIONES
    // =========================
    private void aplicarDtoAEntidad(ObraDto dto, Obra obra) {
        if (dto.getTitulo() == null || dto.getTitulo().isBlank()) {
            throw new IllegalArgumentException("El titulo es obligatorio");
        }
        if (dto.getTipo() == null) {
            throw new IllegalArgumentException("El tipo es obligatorio");
        }
        if (dto.getEstado() == null) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }
        if (dto.getUsuarioId() == null) {
            throw new IllegalArgumentException("El usuario creador es obligatorio");
        }
        if (dto.getPlataformaNombre() == null || dto.getPlataformaNombre().isBlank()) {
            throw new IllegalArgumentException("La plataforma es obligatoria");
        }

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        String nombrePlataforma = dto.getPlataformaNombre().trim();
        Plataforma plataforma = plataformaRepository.findByNombre(nombrePlataforma)
                .orElseGet(() -> {
                    Plataforma nueva = new Plataforma();
                    nueva.setNombre(nombrePlataforma);
                    return plataformaRepository.save(nueva);
                });

        obra.setTitulo(dto.getTitulo());
        obra.setTipo(dto.getTipo());
        obra.setDescripcion(dto.getDescripcion());
        obra.setFechaLanzamiento(dto.getFechaLanzamiento());
        obra.setEstado(dto.getEstado());
        if (obra.getFechaRegistro() == null) {
            obra.setFechaRegistro(LocalDateTime.now());
        }
        obra.setPlataforma(plataforma);
        obra.setCreador(usuario);
    }

    // =========================
    // ZONA 5: REGISTRO DE EVENTOS
    // =========================
    private void registrarEvento(String tipoEvento, Obra obra) {
        Evento evento = new Evento();
        evento.setTimestamp(Instant.now());
        evento.setUserId(obra.getCreador() != null ? obra.getCreador().getId() : null);
        evento.setEntityType("Obra");
        evento.setEntityId(obra.getId());
        evento.setType(tipoEvento);

        Map<String, Object> payload = new HashMap<>();
        payload.put("titulo", obra.getTitulo());
        payload.put("estado", obra.getEstado() != null ? obra.getEstado().name() : null);
        payload.put("tipo", obra.getTipo() != null ? obra.getTipo().name() : null);
        evento.setPayload(payload);

        eventoRepository.save(evento);
    }

    private void registrarEventoDelete(Obra obra) {
        Evento evento = new Evento();
        evento.setTimestamp(Instant.now());
        evento.setUserId(obra.getCreador() != null ? obra.getCreador().getId() : null);
        evento.setEntityType("Obra");
        evento.setEntityId(obra.getId());
        evento.setType("DELETE_OBRA");

        Map<String, Object> payload = new HashMap<>();
        payload.put("titulo", obra.getTitulo());
        payload.put("estado", obra.getEstado() != null ? obra.getEstado().name() : null);
        payload.put("tipo", obra.getTipo() != null ? obra.getTipo().name() : null);
        payload.put("plataformaNombre", obra.getPlataforma() != null ? obra.getPlataforma().getNombre() : null);
        payload.put("creadorId", obra.getCreador() != null ? obra.getCreador().getId() : null);
        evento.setPayload(payload);

        eventoRepository.save(evento);
    }

    private Map<String, Object> snapshotObra(Obra obra) {
        Map<String, Object> snapshot = new HashMap<>();
        snapshot.put("id", obra.getId());
        snapshot.put("titulo", obra.getTitulo());
        snapshot.put("tipo", obra.getTipo() != null ? obra.getTipo().name() : null);
        snapshot.put("descripcion", obra.getDescripcion());
        snapshot.put("fechaLanzamiento", obra.getFechaLanzamiento());
        snapshot.put("fechaRegistro", obra.getFechaRegistro());
        snapshot.put("estado", obra.getEstado() != null ? obra.getEstado().name() : null);
        snapshot.put("plataformaNombre", obra.getPlataforma() != null ? obra.getPlataforma().getNombre() : null);
        snapshot.put("creadorId", obra.getCreador() != null ? obra.getCreador().getId() : null);
        return snapshot;
    }
}
