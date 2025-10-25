package model.dao;

import model.entities.Aluno;

public interface AlunoDao extends Dao<Aluno>{
    Aluno buscaPorNome(String nome);

}
