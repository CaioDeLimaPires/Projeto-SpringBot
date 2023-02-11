package com.Muralis.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Muralis.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento,Long> {

}
