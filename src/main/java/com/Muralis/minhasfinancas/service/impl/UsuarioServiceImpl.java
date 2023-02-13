package com.Muralis.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.Muralis.minhasfinancas.exception.ErroAutenticacao;
import com.Muralis.minhasfinancas.exception.RegraNegocioException;
import com.Muralis.minhasfinancas.model.entity.Usuario;
import com.Muralis.minhasfinancas.model.repository.UsuarioRepository;
import com.Muralis.minhasfinancas.service.UsuarioService;

import jakarta.transaction.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioService{
	
	private UsuarioRepository repository;
	
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}
	
	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario =  repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuario não encontrado para o email informado.");
		}
		
		if (!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida.");
		}
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvaUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		
		if(existe) {
			throw new RegraNegocioException("Já existe um usuario cadastrado com esse email.");
		}
		
	}

	@Override
	public Optional <Usuario> obterPorId(Long id) {
		return repository.findById(id);
	}

}
