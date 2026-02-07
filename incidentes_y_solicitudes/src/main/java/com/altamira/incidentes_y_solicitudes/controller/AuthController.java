package com.altamira.incidentes_y_solicitudes.controller;

import com.altamira.incidentes_y_solicitudes.controller.dto.LoginRequestDTO;
import com.altamira.incidentes_y_solicitudes.controller.dto.LoginResponseDTO;
import com.altamira.incidentes_y_solicitudes.controller.dto.UsuarioDTO;
import com.altamira.incidentes_y_solicitudes.persistence.entity.Usuario;
import com.altamira.incidentes_y_solicitudes.service.UsuarioService;
import com.altamira.incidentes_y_solicitudes.service.jwt.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public AuthController(UsuarioService usuarioService, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        log.info("Intento de login del usuario: {}", loginRequest.getUsuario());

        // Validar credenciales
        if (!usuarioService.validarCredenciales(loginRequest.getUsuario(), loginRequest.getContrasena())) {
            log.warn("Login fallido para usuario: {}", loginRequest.getUsuario());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuario o contrasena incorrectos");
        }

        // Obtener datos del usuario
        Usuario usuario = usuarioService.obtenerEntidadPorUsuario(loginRequest.getUsuario());
        if (usuario == null || !usuario.getActivo()) {
            log.warn("Usuario inactivo: {}", loginRequest.getUsuario());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Usuario inactivo");
        }

        // Generar token JWT
        String token = jwtService.generateToken(
                usuario.getUsuario(),
                usuario.getNombre() + " " + usuario.getApellido(),
                usuario.getRol().getNombre()
        );

        log.info("Login exitoso para usuario: {}", loginRequest.getUsuario());

        LoginResponseDTO response = LoginResponseDTO.builder()
                .token(token)
                .usuario(usuario.getUsuario())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .rol(usuario.getRol().getNombre())
                .usuarioId(usuario.getId())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        log.info("Registro de nuevo usuario: {}", usuarioDTO.getUsuario());

        try {
            usuarioDTO.setCreadoPor("SYSTEM");
            UsuarioDTO usuarioCreado = usuarioService.crear(usuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
        } catch (Exception e) {
            log.error("Error en registro de usuario", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al registrar usuario: " + e.getMessage());
        }
    }

    @GetMapping("/validar-token")
    public ResponseEntity<?> validarToken(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Token no valido");
        }

        String jwtToken = token.substring(7);
        if (jwtService.isTokenValid(jwtToken)) {
            return ResponseEntity.ok("Token valido");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token expirado o invalido");
    }
}

