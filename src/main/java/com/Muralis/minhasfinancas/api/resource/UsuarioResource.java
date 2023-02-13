package com.Muralis.minhasfinancas.api.resource;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Muralis.minhasfinancas.api.dto.UsuarioDTO;
import com.Muralis.minhasfinancas.exception.ErroAutenticacao;
import com.Muralis.minhasfinancas.exception.RegraNegocioException;
import com.Muralis.minhasfinancas.model.entity.Usuario;
import com.Muralis.minhasfinancas.service.LancamentoService;
import com.Muralis.minhasfinancas.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioResource {

	// final serve para declarar dependencia e para que funcione
	// deve utiliza a notação @RequiredArgsConstructor
	private final UsuarioService service;
	private final LancamentoService lancamentoService;

	/*
	 * Injeção de dependencia construida 
	 * public UsuarioResource(UsuarioServiceservice) 
	 * { 
	 * 		this.service=service; 
	 * }
	 
	 */
	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) {
		try {
			Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
			return ResponseEntity.ok(usuarioAutenticado);
		} catch (ErroAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// @RequestBody converte o OBJ JSON em usuarioDTO e exige
	// que o valor das prpriedades venham com os nomes declarados na classe dto
	// email,senha e nome

	@PostMapping
	public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {

		Usuario usuario = Usuario.builder().nome(dto.getNome()).email(dto.getEmail()).senha(dto.getSenha()).build();

		try {
			Usuario usuarioSalvo = service.salvaUsuario(usuario);

			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);

		} catch (RegraNegocioException e) {

			return ResponseEntity.badRequest().body(e.getMessage());

		}

	}
	@GetMapping("{id}/saldo")
	public ResponseEntity obterSaldo(@PathVariable("id") Long id) {
		Optional<Usuario> usuario = service.obterPorId(id);
		
		if(!usuario.isPresent()) {
			
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);
		return ResponseEntity.ok(saldo);
	}
}
