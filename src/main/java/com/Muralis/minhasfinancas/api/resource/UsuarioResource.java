package com.Muralis.minhasfinancas.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Muralis.minhasfinancas.api.dto.UsuarioDTO;
import com.Muralis.minhasfinancas.exception.ErroAutenticacao;
import com.Muralis.minhasfinancas.exception.RegraNegocioException;
import com.Muralis.minhasfinancas.model.entity.Usuario;
import com.Muralis.minhasfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

	// Interface o conteiner de injeção do spring busca uma implementação e adiciona
	private UsuarioService service;

	//Injeção de dependencia 
	public UsuarioResource(UsuarioService service) {
		this.service=service;
	}
	
	@PostMapping("/autenticar")
	public ResponseEntity autenticar (@RequestBody UsuarioDTO dto) {
		try {
			Usuario usuarioAutenticado = service.autenticar(dto.getEmail(),dto.getSenha());
		 return ResponseEntity.ok(usuarioAutenticado);
		}
		catch(ErroAutenticacao e)
		{
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// @RequestBody converte o OBJ JSON em usuarioDTO e exige
	// que o valor das prpriedades venham com os nomes declarados na classe dto
	// email,senha e nome
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {
		
		Usuario usuario = Usuario.builder()
				.nome(dto.getNome())
				.email(dto.getEmail())
				.senha(dto.getSenha()).build();
		
		try {
			Usuario usuarioSalvo = service.salvaUsuario(usuario);
			
			return new ResponseEntity(usuarioSalvo,HttpStatus.CREATED);
			
		}
		catch(RegraNegocioException e) {
			
			return ResponseEntity.badRequest().body(e.getMessage());
			
		}

	}
}
