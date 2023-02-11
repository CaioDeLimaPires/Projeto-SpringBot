package com.Muralis.minhasfinancas.service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.mock.mockito.SpyBeans;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.Muralis.minhasfinancas.exception.ErroAutenticacao;
import com.Muralis.minhasfinancas.exception.RegraNegocioException;
import com.Muralis.minhasfinancas.model.entity.Usuario;
import com.Muralis.minhasfinancas.model.repository.UsuarioRepository;
import com.Muralis.minhasfinancas.service.impl.UsuarioServiceImpl;

import junit.framework.Assert;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@SpyBean
	UsuarioServiceImpl service;	
	
	@MockBean
	UsuarioRepository repository;
	
	@Test
	public void deveSalvarUmUsuario() {		
		//cenario
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario= Usuario.builder()
				.id(1l)
				.nome("nome")
				.email("email@email.com")
				.senha("senha").build();
		
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//acao
		Usuario usuarioSalvo= service.salvaUsuario(new Usuario());
		
		//verificacao
		Assertions.assertNotNull(usuarioSalvo);		
		Assertions.assertEquals(usuarioSalvo.getId(),1l);
		Assertions.assertEquals(usuarioSalvo.getNome(),"nome");
		Assertions.assertEquals(usuarioSalvo.getEmail(),"email@email.com");
		Assertions.assertEquals(usuarioSalvo.getSenha(),"senha");
	
		

	}
	
	@Test
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {

		//cenario
		String email= "email@email.com";
		Usuario usuario = Usuario.builder().email(email).build();		
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		
		 Assertions.assertThrows(RegraNegocioException.class, ()-> {
		//acao
		service.salvaUsuario(usuario);
		});	
		
		//verificacao
		Mockito.verify(repository, Mockito.never()).save(usuario);
	
		
	}
	
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		//cenario
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		//acao
		Usuario result = service.autenticar(email, senha);
		
		//verificacao
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {	
	
		//cenario
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		Throwable assertThrows = Assertions.assertThrows(ErroAutenticacao.class, ()-> {
	
			//acao
			service.autenticar("email@email.com","senha");
		});
		//verificacao
		Assertions
		.assertEquals(assertThrows
				.getMessage(), "Usuario não encontrado para o email informado." );
	}
	
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		//cenario
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		Throwable assertThrows = Assertions.assertThrows(ErroAutenticacao.class, ()-> {
			
			//acao
			service.autenticar("email@email.com","123");
		});
		//verificacao
		Assertions.assertEquals(assertThrows.getMessage(),"Senha inválida." );
		
	}

	@Test
	public void deveValidarEmail() {

		Assertions.assertDoesNotThrow(() -> {

			// cenário
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

			// acao
			service.validarEmail("email@email.com");
		});
	}
	
	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			// cenario
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
			// acao
			service.validarEmail("email@email.com");
		});
	}

}