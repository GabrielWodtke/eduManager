package model.dao;

import db.DB;
import model.dao.impl.AlunoDaoJDBC;
import model.dao.impl.CursoDaoJDBC;
import model.dao.impl.MatriculaDaoJDBC;

public class DaoFactory {
    public static AlunoDao createAlunoDao(){
        return new AlunoDaoJDBC(DB.getConnection());
    }

    public static CursoDao createCursoDao(){
        return new CursoDaoJDBC(DB.getConnection());
    }

    public static MatriculaDao createMatriculaDao(){
        return new MatriculaDaoJDBC(DB.getConnection());
    }
}
