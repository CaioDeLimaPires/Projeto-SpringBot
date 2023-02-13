package com.Muralis.minhasfinancas.service;

import java.util.Optional;

import com.Muralis.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {
	Usuario autenticar(String email, String senha);
		
	void validarEmail(String email);

	Usuario salvaUsuario(Usuario usuario);

	Optional <Usuario> obterPorId(Long id);
}
