package com.tuapp.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tuapp.domain.Obra;
import com.tuapp.domain.Resena;
import com.tuapp.domain.Usuario;
import com.tuapp.dto.OpcionDto;
import com.tuapp.dto.ResenaDto;
import com.tuapp.mongo.Evento;
import com.tuapp.mongo.EventoRepository;
import com.tuapp.repository.ObraRepository;
import com.tuapp.repository.ResenaRepository;
import com.tuapp.repository.UsuarioRepository;

// Servicio de negocio para gestionar reseñas y registrar su trazabilidad en eventos.
@Service
public class ResenaService {

    // =========================
    // ZONA 1: DEPENDENCIAS
    // =========================
    private final ResenaRepository resenaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ObraRepository obraRepository;
    private final EventoRepository eventoRepository;

    // =========================
    // ZONA 2: CONSTRUCTOR
    // =========================
    public ResenaService(ResenaRepository resenaRepository,
            UsuarioRepository usuarioRepository,
            ObraRepository obraRepository,
            EventoRepository eventoRepository) {
        this.resenaRepository = resenaRepository;
        this.usuarioRepository = usuarioRepository;
        this.obraRepository = obraRepository;
        this.eventoRepository = eventoRepository;
    }

    // =========================
    // ZONA 3: OPERACIONES DE NEGOCIO
    // =========================
    public Resena crearResena(ResenaDto dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

        Obra obra = obraRepository.findById(dto.getObraId())
                .orElseThrow(() -> new NoSuchElementException("Obra no encontrada"));

        Resena resena = new Resena();
        resena.setUsuario(usuario);
        resena.setObra(obra);
        resena.setNota(dto.getNota());
        resena.setComentario(dto.getComentario());
        resena.setFecha(LocalDateTime.now());

        Resena guardada = resenaRepository.save(resena);
        registrarEventoCreate(guardada);
        return guardada;
    }

    public Resena actualizarResena(Long id, ResenaDto dto) {
        Resena existente = resenaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resena no encontrada"));

        // Evento con el estado anterior
        registrarEventoUpdateAnterior(existente);

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

        Obra obra = obraRepository.findById(dto.getObraId())
                .orElseThrow(() -> new NoSuchElementException("Obra no encontrada"));

        existente.setUsuario(usuario);
        existente.setObra(obra);
        existente.setNota(dto.getNota());
        existente.setComentario(dto.getComentario());
        existente.setFecha(LocalDateTime.now());

        return resenaRepository.save(existente);
    }

    public List<Resena> listarPorObra(Long obraId) {
        return resenaRepository.findByObraId(obraId);
    }

    public List<Resena> listarPorObras(List<Long> obraIds) {
        if (obraIds == null || obraIds.isEmpty()) {
            return List.of();
        }
        return resenaRepository.findByObraIdIn(obraIds);
    }

    public List<OpcionDto> listarOpcionesUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuario -> new OpcionDto(usuario.getId(), usuario.getNombre()))
                .collect(Collectors.toList());
    }

    public List<OpcionDto> listarOpcionesObras() {
        return obraRepository.findAll()
                .stream()
                .map(obra -> new OpcionDto(obra.getId(), obra.getTitulo()))
                .collect(Collectors.toList());
    }

    public void eliminarResena(Long id) {
        Resena resena = resenaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resena no encontrada"));

        registrarEventoDelete(resena);
        resenaRepository.delete(resena);
    }

    // =========================
    // ZONA 4: REGISTRO DE EVENTOS
    // =========================
    private void registrarEventoCreate(Resena resena) {
        Evento evento = new Evento();
        evento.setTimestamp(Instant.now());
        evento.setUserId(resena.getUsuario() != null ? resena.getUsuario().getId() : null);
        evento.setEntityType("RESENA");
        evento.setEntityId(resena.getId());
        evento.setType("CREATE_RESENA");

        Map<String, Object> payload = new HashMap<>();
        payload.put("nota", resena.getNota());
        payload.put("comentario", resena.getComentario());
        payload.put("obraId", resena.getObra() != null ? resena.getObra().getId() : null);
        payload.put("usuarioId", resena.getUsuario() != null ? resena.getUsuario().getId() : null);
        evento.setPayload(payload);

        eventoRepository.save(evento);
    }

    private void registrarEventoUpdateAnterior(Resena resena) {
        Evento evento = new Evento();
        evento.setTimestamp(Instant.now());
        evento.setUserId(resena.getUsuario() != null ? resena.getUsuario().getId() : null);
        evento.setEntityType("RESENA");
        evento.setEntityId(resena.getId());
        evento.setType("UPDATE_RESENA");

        Map<String, Object> payload = new HashMap<>();
        payload.put("nota", resena.getNota());
        payload.put("comentario", resena.getComentario());
        payload.put("obraId", resena.getObra() != null ? resena.getObra().getId() : null);
        payload.put("usuarioId", resena.getUsuario() != null ? resena.getUsuario().getId() : null);
        evento.setPayload(payload);

        eventoRepository.save(evento);
    }

    private void registrarEventoDelete(Resena resena) {
        Evento evento = new Evento();
        evento.setTimestamp(Instant.now());
        evento.setUserId(resena.getUsuario() != null ? resena.getUsuario().getId() : null);
        evento.setEntityType("RESENA");
        evento.setEntityId(resena.getId());
        evento.setType("DELETE_RESENA");

        Map<String, Object> payload = new HashMap<>();
        payload.put("nota", resena.getNota());
        payload.put("comentario", resena.getComentario());
        payload.put("obraId", resena.getObra() != null ? resena.getObra().getId() : null);
        payload.put("usuarioId", resena.getUsuario() != null ? resena.getUsuario().getId() : null);
        evento.setPayload(payload);

        eventoRepository.save(evento);
    }
}
