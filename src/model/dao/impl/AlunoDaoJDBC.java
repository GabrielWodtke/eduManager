package model.dao.impl;

import db.DB;
import db.DBexception;
import model.dao.AlunoDao;
import model.entities.Aluno;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlunoDaoJDBC implements AlunoDao {
    Connection conn;

    public AlunoDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Aluno> buscaPorNome(String nome) {
        return findAll().stream().filter(aluno -> aluno.getNome().equalsIgnoreCase(nome)).toList();
    }

    @Override
    public void insert(Aluno obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("INSERT INTO aluno(Nome, Email, DataNascimento) VALUES(?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, obj.getNome());
            st.setString(2, obj.getEmail());
            st.setDate(3, java.sql.Date.valueOf(obj.getDataNascimento()));

            int rowsAffected = st.executeUpdate();

            if(rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);
                    System.out.println("Aluno inserido com sucesso, id: " + id);
                }
                DB.closeResultSet(rs);
            }
            else {
                throw new DBexception("error! no rows were affected");
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
    public void update(Aluno obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("UPDATE aluno SET Nome = ?, Email = ?, DataNascimento = ? WHERE id = ?");
            st.setString(1, obj.getNome());
            st.setString(2, obj.getEmail());
            st.setDate(3, Date.valueOf(obj.getDataNascimento()));
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
            st = conn.prepareStatement("DELETE FROM aluno WHERE id = ?");
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
    public Optional<Aluno> findById(Integer id) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("SELECT aluno.* FROM aluno WHERE id = ?");
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();
            if(rs.next()){
                Aluno obj = istantiateAluno(rs);
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
    public List<Aluno> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Aluno> aluninhos = new ArrayList<>();
        try{
            st = conn.prepareStatement("SELECT * FROM aluno");
            rs= st.executeQuery();

            while(rs.next()){
                aluninhos.add(istantiateAluno(rs));
            }
            return aluninhos;
        }
        catch(SQLException e){
            throw new DBexception(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    public Aluno istantiateAluno(ResultSet rs) throws SQLException {
        Aluno obj = new Aluno();
        obj.setId(rs.getInt("id"));
        obj.setDataNascimento(rs.getDate("DataNascimento").toLocalDate());
        obj.setEmail(rs.getString("Email"));
        obj.setNome(rs.getString("Nome"));

        return obj;
    }
}
