package application;

import model.dao.AlunoDao;
import model.dao.CursoDao;
import model.dao.DaoFactory;
import model.dao.MatriculaDao;
import model.entities.Aluno;
import model.entities.Curso;
import model.entities.matricula;

import java.time.LocalDate;
import java.util.List;

public class program {
    public static void main(String[] args) {
        AlunoDao alunoDao = DaoFactory.createAlunoDao();
        CursoDao cursoDao = DaoFactory.createCursoDao();
        MatriculaDao matriculaDao = DaoFactory.createMatriculaDao();
        
    }
}
