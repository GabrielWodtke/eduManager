package model.dao;

import model.entities.matricula;

public interface MatriculaDao extends Dao<matricula> {
    void vincularAlunoCurso();
    void exibirMatriculasNomeAlunoECurso();
    void matriculasPorCurso();
}
