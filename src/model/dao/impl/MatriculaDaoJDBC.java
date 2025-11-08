package model.dao.impl;

import db.DB;
import db.DBexception;
import model.dao.CursoDao;
import model.dao.DaoFactory;
import model.dao.MatriculaDao;
import model.entities.Aluno;
import model.entities.Curso;
import model.entities.matricula;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MatriculaDaoJDBC implements MatriculaDao {
    Connection conn;

    public MatriculaDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void exibirMatriculasNomeAlunoECurso() {
        List<matricula> matriculasBruta = findAll();
        List<String> stringFinal = new ArrayList<>();

        try{
            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            for(matricula m : matriculasBruta){
                String nomeAluno = getNomeAluno(m);
                String nomeCurso = getNomeCurso(m);
                System.out.printf("\n\n----\nId: %d Data: %s \nNome do aluno:%s \nCurso:%s",
                        m.getId(), m.getDataMatricula().format(formatador), nomeAluno, nomeCurso);
            }
        }
        catch(SQLException e){
            throw new DBexception(e.getMessage());
        }
    }

    public String getNomeAluno(matricula m) throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;
        st = conn.prepareStatement("FROM aluno SELECT aluno.nome WHERE id = ?");
        st.setInt(1, m.getIdAluno());
        rs = st.executeQuery();
        if(rs.next()){
            String nome = rs.getString("Nome");
            DB.closeStatement(st);
            DB.closeResultSet(rs);
            return nome;
        }
        else{
            throw new DBexception("Erro: o aluno não foi encontrado");
        }
    }

    public String getNomeCurso(matricula m) throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;
        st = conn.prepareStatement("FROM curso SELECT curso.nome WHERE id = ?");
        st.setInt(1, m.getIdCurso());
        rs = st.executeQuery();
        if(rs.next()){
            String nome = rs.getString("Nome");
            DB.closeStatement(st);
            DB.closeResultSet(rs);
            return nome;
        }
        else{
            throw new DBexception("Erro: o curso não foi encontrado");
        }
    }

    @Override
    public void matriculasPorCurso() {
        List<matricula> matriculas = findAll();
        HashMap<Integer, Integer> matPorCurso = new HashMap<>();
        for(matricula m : matriculas){
            int idCurso = m.getIdCurso();
            if(matPorCurso.containsKey(idCurso)){
                matPorCurso.put(idCurso, matPorCurso.get(idCurso) + 1);
            }
            else{
                matPorCurso.put(idCurso, 1);
            }
        }
        System.out.println(matPorCurso);
        CursoDao cDao = DaoFactory.createCursoDao();
        try {
            for (int i : matPorCurso.keySet()) {
                Curso c = cDao.findById(i).orElse(null);
                System.out.printf("\n----\nCurso:%s \nN. Matriculas:%d \n", c.getNome(), matPorCurso.get(i));
            }
        }
        catch (NullPointerException e){
            System.out.println("Error: existe um id de algum curso nas matriculas que não existe mais." +
                    e.getMessage());
        }
    }

    @Override
    public void insert(matricula obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement(
                    "INSERT INTO matricula(IdAluno, IdCurso, DataMatricula) " +
                            "VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS
            );
            st.setInt(1, obj.getIdAluno());
            st.setInt(2, obj.getIdCurso()   );
            st.setDate(3, Date.valueOf(obj.getDataMatricula()));

            int rowsAffected = st.executeUpdate();
            if(rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            }
            else{
                throw new DBexception("Unexpected error! No rows affected");
            }
        }
        catch(SQLException e){
            throw new DBexception(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(matricula obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("UPDATE matricula SET IdAluno = ?, IdCurso = ?, DataMatricula = ? " +
                    "WHERE id = ?");
            st.setInt(1, obj.getIdAluno());
            st.setInt(2, obj.getIdCurso());
            st.setDate(3, Date.valueOf(obj.getDataMatricula()));
            st.setInt(4, obj.getId());

            int rowsAffected = st.executeUpdate();

            if(rowsAffected > 0){
                System.out.println("Sucess! Rows affected: " + rowsAffected);
            }
            else{
                throw new DBexception("ERROR, no row was affected");
            }
        }
        catch (SQLException e){
            throw new DBexception(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("DELETE FROM matricula WHERE id = ?");
            st.setInt(1, id);

            int rowsAffected = st.executeUpdate();
            if(rowsAffected > 0){
                System.out.println("Sucess! Rows affected: " + rowsAffected);
            }
            else{
                throw new DBexception("ERROR, no row were affected");
            }
        }
        catch (SQLException e){
            throw new DBexception(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Optional<matricula> findById(Integer id) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("SELECT matricula.* FROM matricula WHERE id = ?");
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();
            if(rs.next()){
                matricula obj = instantiateMatricula(rs);
                DB.closeResultSet(rs);
                return Optional.of(obj);
            }
            return Optional.empty();
        }
        catch(SQLException e){
            throw new DBexception(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public List<matricula> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<matricula> matriculas = new ArrayList<>();
        try{
            st = conn.prepareStatement("SELECT * FROM matricula");
            rs= st.executeQuery();

            while(rs.next()){
                matriculas.add(instantiateMatricula(rs));
            }
            return matriculas;
        }
        catch(SQLException e){
            throw new DBexception(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    public matricula instantiateMatricula(ResultSet rs) throws SQLException {
        matricula obj = new matricula();
        obj.setId(rs.getInt("id"));
        obj.setIdAluno(rs.getInt("idAluno"));
        obj.setIdCurso(rs.getInt("IdCurso"));
        obj.setDataMatricula(rs.getDate("DataMatricula").toLocalDate());

        return obj;
    }
}
