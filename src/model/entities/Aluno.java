package model.entities;

import java.time.LocalDate;

public class Aluno {
    private int id;
    private String nome;
    private String email;
    private LocalDate dataNascimento;

    public Aluno(int id, LocalDate dataNascimento, String email, String nome) {
        this.id = id;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.nome = nome;
    }

    public Aluno(){

    }

    public Aluno(LocalDate dataNascimento, String email, String nome) {
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Aluno{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", dataNascimento=" + dataNascimento +
                '}';
    }
}
