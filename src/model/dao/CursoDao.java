package model.dao;

import model.entities.Curso;

public interface CursoDao extends Dao<Curso>{
    void gerarRelatorio();
}
