package model.dao;

import model.entities.Aluno;

import java.util.List;

public interface AlunoDao extends Dao<Aluno>{
    List<Aluno> buscaPorNome(String nome);

}
