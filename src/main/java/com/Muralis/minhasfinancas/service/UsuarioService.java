package com.Muralis.minhasfinancas.service;

import com.Muralis.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {
	Usuario autenticar(String email, String senha);
		
	void validarEmail(String email);

	Usuario salvaUsuario(Usuario usuario);

}
