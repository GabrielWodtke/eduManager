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
import java.util.Optional;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

import db.DB;

public class program {
    public static void main(String[] args) {
        AlunoDao alunoDao = DaoFactory.createAlunoDao();
        CursoDao cursoDao = DaoFactory.createCursoDao();
        MatriculaDao matriculaDao = DaoFactory.createMatriculaDao();
        Scanner sc = new Scanner(System.in);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        boolean running = true;
        while(running){
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Gerenciar Alunos");
            System.out.println("2. Gerenciar Cursos");
            System.out.println("3. Gerenciar Matrículas");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");
            String opt = sc.nextLine().trim();

            switch(opt){
                case "1":
                    manageAlunos(sc, alunoDao, fmt);
                    break;
                case "2":
                    manageCursos(sc, cursoDao, fmt);
                    break;
                case "3":
                    manageMatriculas(sc, matriculaDao, alunoDao, cursoDao, fmt);
                    break;
                case "5":
                    running = false;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }

        sc.close();
        DB.closeConnection();
    }

    private static void manageAlunos(Scanner sc, AlunoDao alunoDao, DateTimeFormatter fmt){
        boolean back = false;
        while(!back){
            System.out.println("\n-- Gerenciar Alunos --");
            System.out.println("1. Listar todos");
            System.out.println("2. Buscar por id");
            System.out.println("3. Buscar por nome");
            System.out.println("4. Inserir");
            System.out.println("5. Atualizar");
            System.out.println("6. Deletar");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            String op = sc.nextLine().trim();

            switch(op){
                case "1":
                    List<Aluno> all = alunoDao.findAll();
                    all.forEach(System.out::println);
                    break;
                case "2":
                    System.out.print("Id: ");
                    Integer id = readInt(sc);
                    Optional<Aluno> a = alunoDao.findById(id);
                    System.out.println(a.orElse(null));
                    break;
                case "3":
                    System.out.print("Nome: ");
                    String nome = sc.nextLine();
                    alunoDao.buscaPorNome(nome).forEach(System.out::println);
                    break;
                case "4":
                    System.out.print("Nome: ");
                    String n = sc.nextLine();
                    System.out.print("Email: ");
                    String e = sc.nextLine();
                    System.out.print("Data nascimento (dd/MM/yyyy): ");
                    LocalDate d = LocalDate.parse(sc.nextLine(), fmt);
                    Aluno novo = new Aluno(d, e, n);
                    alunoDao.insert(novo);
                    break;
                case "5":
                    System.out.print("Id do aluno a atualizar: ");
                    Integer uid = readInt(sc);
                    Optional<Aluno> uOpt = alunoDao.findById(uid);
                    if(uOpt.isPresent()){
                        Aluno up = uOpt.get();
                        System.out.print("Novo nome (enter para manter '"+up.getNome()+"'): ");
                        String nn = sc.nextLine(); if(!nn.isBlank()) up.setNome(nn);
                        System.out.print("Novo email (enter para manter '"+up.getEmail()+"'): ");
                        String ne = sc.nextLine(); if(!ne.isBlank()) up.setEmail(ne);
                        System.out.print("Nova data nascimento dd/MM/yyyy (enter para manter '"+up.getDataNascimento()+"'): ");
                        String nd = sc.nextLine(); if(!nd.isBlank()) up.setDataNascimento(LocalDate.parse(nd, fmt));
                        alunoDao.update(up);
                    } else System.out.println("Aluno não encontrado.");
                    break;
                case "6":
                    System.out.print("Id do aluno a deletar: ");
                    Integer did = readInt(sc);
                    alunoDao.deleteById(did);
                    break;
                case "0":
                    back = true; break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }


    private static void manageCursos(Scanner sc, CursoDao cursoDao, DateTimeFormatter fmt){
        boolean back = false;
        while(!back){
            System.out.println("\n-- Gerenciar Cursos --");
            System.out.println("1. Listar todos");
            System.out.println("2. Buscar por id");
            System.out.println("3. Inserir");
            System.out.println("4. Atualizar");
            System.out.println("5. Deletar");
            System.out.println("6. Gerar relatório");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            String op = sc.nextLine().trim();

            switch(op){
                case "1":
                    cursoDao.findAll().forEach(System.out::println);
                    break;
                case "2":
                    System.out.print("Id: ");
                    Integer id = readInt(sc);
                    Optional<Curso> c = cursoDao.findById(id);
                    System.out.println(c.orElse(null));
                    break;
                case "3":
                    System.out.print("Nome: ");
                    String nome = sc.nextLine();
                    System.out.print("Carga horária (int): ");
                    Integer ch = readInt(sc);
                    System.out.print("Valor (decimal): ");
                    Double val = readDouble(sc);
                    Curso novo = new Curso(nome, ch, val);
                    cursoDao.insert(novo);
                    break;
                case "4":
                    System.out.print("Id do curso a atualizar: ");
                    Integer uid = readInt(sc);
                    Optional<Curso> uOpt = cursoDao.findById(uid);
                    if(uOpt.isPresent()){
                        Curso up = uOpt.get();
                        System.out.print("Novo nome (enter para manter '"+up.getNome()+"'): ");
                        String nn = sc.nextLine(); if(!nn.isBlank()) up.setNome(nn);
                        System.out.print("Nova carga horaria (enter para manter '"+up.getCargaHoraria()+"'): ");
                        String nch = sc.nextLine(); if(!nch.isBlank()) up.setCargaHoraria(Integer.parseInt(nch));
                        System.out.print("Novo valor (enter para manter '"+up.getValor()+"'): ");
                        String nv = sc.nextLine(); if(!nv.isBlank()) up.setValor(Double.parseDouble(nv));
                        cursoDao.update(up);
                    } else System.out.println("Curso não encontrado.");
                    break;
                case "5":
                    System.out.print("Id do curso a deletar: ");
                    Integer did = readInt(sc);
                    cursoDao.deleteById(did);
                    break;
                case "6":
                    cursoDao.gerarRelatorio();
                    break;
                case "0":
                    back = true; break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void manageMatriculas(Scanner sc, MatriculaDao matriculaDao, AlunoDao alunoDao, CursoDao cursoDao, DateTimeFormatter fmt){
        boolean back = false;
        while(!back){
            System.out.println("\n-- Gerenciar Matrículas --");
            System.out.println("1. Listar todos");
            System.out.println("2. Buscar por id");
            System.out.println("3. Inserir");
            System.out.println("4. Atualizar");
            System.out.println("5. Deletar");
            System.out.println("6. Exibir matriculas (nome aluno e curso)");
            System.out.println("7. Quantidade de matriculas por curso");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            String op = sc.nextLine().trim();

            switch(op){
                case "1":
                    matriculaDao.findAll().forEach(System.out::println);
                    break;
                case "2":
                    System.out.print("Id: ");
                    Integer id = readInt(sc);
                    Optional<matricula> m = matriculaDao.findById(id);
                    System.out.println(m.orElse(null));
                    break;
                case "3":
                    System.out.print("Id do Aluno: ");
                    Integer idAluno = readInt(sc);
                    System.out.print("Id do Curso: ");
                    Integer idCurso = readInt(sc);
                    System.out.print("Data matrícula (dd/MM/yyyy): ");
                    LocalDate dt = LocalDate.parse(sc.nextLine(), fmt);
                    matricula novo = new matricula(dt, idCurso, idAluno);
                    matriculaDao.insert(novo);
                    break;
                case "4":
                    System.out.print("Id da matrícula a atualizar: ");
                    Integer uid = readInt(sc);
                    Optional<matricula> uOpt = matriculaDao.findById(uid);
                    if(uOpt.isPresent()){
                        matricula up = uOpt.get();
                        System.out.print("Novo idAluno (enter para manter '"+up.getIdAluno()+"'): ");
                        String na = sc.nextLine(); if(!na.isBlank()) up.setIdAluno(Integer.parseInt(na));
                        System.out.print("Novo idCurso (enter para manter '"+up.getIdCurso()+"'): ");
                        String nc = sc.nextLine(); if(!nc.isBlank()) up.setIdCurso(Integer.parseInt(nc));
                        System.out.print("Nova data dd/MM/yyyy (enter para manter '"+up.getDataMatricula()+"'): ");
                        String nd = sc.nextLine(); if(!nd.isBlank()) up.setDataMatricula(LocalDate.parse(nd, fmt));
                        matriculaDao.update(up);
                    } else System.out.println("Matrícula não encontrada.");
                    break;
                case "5":
                    System.out.print("Id da matrícula a deletar: ");
                    Integer did = readInt(sc);
                    matriculaDao.deleteById(did);
                    break;
                case "6":
                    matriculaDao.exibirMatriculasNomeAlunoECurso();
                    break;
                case "7":
                    matriculaDao.matriculasPorCurso();
                    break;
                case "0":
                    back = true; break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static Integer readInt(Scanner sc){
        while(true){
            String s = sc.nextLine().trim();
            try{ return Integer.parseInt(s);}catch(NumberFormatException e){ System.out.print("Número inválido. Tente novamente: "); }
        }
    }

    private static Double readDouble(Scanner sc){
        while(true){
            String s = sc.nextLine().trim();
            try{ return Double.parseDouble(s);}catch(NumberFormatException e){ System.out.print("Número inválido. Tente novamente: "); }
        }
    }
}
