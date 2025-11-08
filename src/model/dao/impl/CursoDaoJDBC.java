package model.dao.impl;

import db.DB;
import db.DBexception;
import model.dao.CursoDao;
import model.entities.Aluno;
import model.entities.Curso;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class CursoDaoJDBC implements CursoDao {
    Connection conn;

    public CursoDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void gerarRelatorio() {
        List<Curso> todosCursos = findAll();
        Double mediaCargaHoraria = todosCursos.stream().mapToDouble(Curso::getCargaHoraria).average().orElse(0.0);
        Double cursoMaisCaro = todosCursos.stream().mapToDouble(Curso::getCargaHoraria).max().orElse(0.0);
        PreparedStatement st = null;
        ResultSet rs = null;
        double valorTotal = 0.0;
        try{
            st = conn.prepareStatement("SELECT matricula.IdCurso \nFROM matricula");
            rs = st.executeQuery();
            HashMap<Integer, Double> valores = new HashMap<>();
            while(rs.next()){
                int idCurso = rs.getInt("idCurso");
                if(valores.containsKey(idCurso)){
                    valores.put(idCurso, valores.get(idCurso) + getValorCurso(idCurso));
                }
                else{
                    valores.put(idCurso, getValorCurso(idCurso));
                }
            }
            valorTotal = valores.values().stream().mapToDouble(p -> p).sum();
        }
        catch(SQLException e){
            throw new DBexception(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
        System.out.println("#-#-# RELATÓRIO CURSO #-#-#\nMedia carga Horaria: " + mediaCargaHoraria +
                "\nCurso mais caro: " + cursoMaisCaro + "\nTotal arrecado: " + valorTotal);
    }

    public double getValorCurso(int id) throws SQLException {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("SELECT curso.Valor FROM curso WHERE Id = ?");
            st.setInt(1, id);

            rs = st.executeQuery();
            if(rs.next()){
                return rs.getDouble("Valor");
            }
            else{
                throw new DBexception("Nenhum curso correspondente ao id foi encontrado.");
            }
        }
        finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public void insert(Curso obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("INSERT INTO curso(Nome, CargaHoraria, Valor) VALUES(?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, obj.getNome());
            st.setInt(2, obj.getCargaHoraria());
            st.setDouble(3, obj.getValor());

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            }
        } catch (SQLException e) {
            throw new DBexception(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Curso obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("UPDATE curso SET Nome = ?, cargaHoraria = ?, Valor = ? WHERE id = ?");
            st.setString(1, obj.getNome());
            st.setInt(2, obj.getCargaHoraria());
            st.setDouble(3, obj.getValor());
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
            st = conn.prepareStatement("DELETE FROM curso WHERE id = ?");
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
    public Optional<Curso> findById(Integer id) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("SELECT curso.* FROM curso WHERE id = ?");
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();
            if(rs.next()){
                Curso obj = istantiateCurso(rs);
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
    public List<Curso> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Curso> cursoo = new ArrayList<>();
        try{
            st = conn.prepareStatement("SELECT * FROM curso");
            rs= st.executeQuery();

            while(rs.next()){
                cursoo.add(istantiateCurso(rs));
            }
            return cursoo;
        }
        catch(SQLException e){
            throw new DBexception(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    public Curso istantiateCurso(ResultSet rs) throws SQLException {
        Curso obj = new Curso();
        obj.setId(rs.getInt("id"));
        obj.setNome(rs.getString("Nome"));
        obj.setValor(rs.getDouble("Valor"));
        obj.setCargaHoraria(rs.getInt("cargaHoraria"));

        return obj;
    }
}
