package model.entities;

public class Curso {
    private int id;
    private String nome;
    private int cargaHoraria;
    private Double valor;

    public Curso() {
    }

    public Curso(String nome, int cargaHoraria, Double valor) {
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.valor = valor;
    }

    public Curso(int id, Double valor, int cargaHoraria, String nome) {
        this.id = id;
        this.valor = valor;
        this.cargaHoraria = cargaHoraria;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Curso{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cargaHoraria=" + cargaHoraria +
                ", valor=" + valor +
                '}';
    }
}
