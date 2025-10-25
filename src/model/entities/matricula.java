package model.entities;

import java.time.LocalDate;

public class matricula {
    private int id;
    private int idAluno;
    private int idCurso;
    private LocalDate dataMatricula;

    public matricula() {
    }

    public matricula(LocalDate dataMatricula, int idCurso, int idAluno) {
        this.dataMatricula = dataMatricula;
        this.idCurso = idCurso;
        this.idAluno = idAluno;
    }

    public matricula(int id, int idCurso, LocalDate dataMatricula, int idAluno) {
        this.id = id;
        this.idCurso = idCurso;
        this.dataMatricula = dataMatricula;
        this.idAluno = idAluno;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDataMatricula() {
        return dataMatricula;
    }

    public void setDataMatricula(LocalDate dataMatricula) {
        this.dataMatricula = dataMatricula;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public int getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(int idAluno) {
        this.idAluno = idAluno;
    }

    @Override
    public String toString() {
        return "matricula{" +
                "id=" + id +
                ", idAluno=" + idAluno +
                ", idCurso=" + idCurso +
                ", dataMatricula=" + dataMatricula +
                '}';
    }
}
