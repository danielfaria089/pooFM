package FootballManager.Model.Equipas;

import FootballManager.Model.Eventos.Eventos;
import FootballManager.Model.Exceptions.EventoInvalidoException;
import FootballManager.Model.Exceptions.JogadorInexistenteException;
import FootballManager.Model.Exceptions.TaticaInvalidaException;
import FootballManager.Model.Players.*;
import FootballManager.Model.Equipas.Taticas.Tatica;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Equipa implements Comparable<Equipa>, Serializable {
    private String nome;
    private Map<Integer, Jogador> jogadores;
    private Tatica tatica;

    //Construtores

    public Equipa() {
        this.nome = "";
        this.jogadores = new TreeMap<>();
    }

    public Equipa(String nome, Map<Integer, Jogador> jogadores){
        this.setNome(nome);
        this.setJogadores(jogadores);
    }

    public Equipa(Equipa eq) {
        this.nome = eq.getNome();
        this.jogadores = eq.getJogadores();
        if(eq.tatica!=null)this.tatica=eq.tatica;
        else this.tatica=null;
    }

    //Equals, clone, etc...

    @Override
    public int compareTo(Equipa o) {
        return nome.compareTo(o.getNome());
    }

    public boolean equals(Object eq) {
        if (eq == null) return false;
        if (eq == this) return true;
        if (this.getClass() != eq.getClass()) return false;
        Equipa nova = (Equipa) eq;
        return nome.equals(nova.getNome());
    }

    public Equipa clone() {
        return new Equipa(this);
    }

    public String align(String input){
        int init=95-input.length();
        return "-".repeat(Math.max(0, init / 2))
                + input +
                "-".repeat(Math.max(0, 95 - (init / 2 + input.length())));
    }

    public String prettyString(){
        StringBuilder sb = new StringBuilder();
        sb.append(align(nome));
        for(Integer i:jogadores.keySet()){
            sb.append("\n");
            sb.append(jogadores.get(i).prettyString());
        }
        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Equipa:").append(this.nome);
        for (Jogador i : this.jogadores.values()) {
            sb.append("\n");
            sb.append(i.toString());
        }
        return sb.toString();
    }

    //Gets e Sets

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Map<Integer, Jogador> getJogadores() {
        return this.jogadores.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, a -> a.getValue().clone()));
    }

    public void setJogadores(Map<Integer, Jogador> jogadores) {
        this.jogadores = jogadores.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, a -> a.getValue().clone()));
    }

    public Jogador getJogador(int nCam) throws JogadorInexistenteException {
        if (!jogadores.containsKey(nCam)) throw new JogadorInexistenteException();
        else return jogadores.get(nCam).clone();
    }

    public void setTatica(Tatica tatica){
        this.tatica=tatica.clone();
    }

    //Calculos

    public int defesa() throws TaticaInvalidaException {
        if(tatica==null)throw new TaticaInvalidaException("Nenhuma","nao foi colocada uma tatica");
        else return (int)Math.round(tatica.defesa(this));
    }

    public int ataque() throws TaticaInvalidaException {
        if(tatica==null)throw new TaticaInvalidaException("Nenhuma","nao foi colocada uma tatica");
        else return (int)Math.round(tatica.ataque(this));
    }

    public int calculaRatingTotal() throws TaticaInvalidaException {
        if(tatica==null)throw new TaticaInvalidaException("Nenhuma","nao foi colocada uma tatica");
        else return (int) ((this.ataque() + this.defesa()) / 2.0);
    }

    //manipular equipa

    public void addJogador(Jogador j) {
        if (j == null) return;
        jogadores.put(j.getNumero(), j.clone());
    }

    public boolean rmvJogador(Jogador j) {
        try {
            if (jogadores.remove(j.getNumero()) == null) {
                throw new JogadorInexistenteException("Jogador:" + j.getNome() + " N:" + j.getNumero() + " inexistente");
            } else return true;

        } catch (JogadorInexistenteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void substituicao(Jogador in,Jogador out){
        tatica.substituicao(in,out);
    }

    public double chanceCruzamento(){
        return this.tatica.ratioCruzamento();
    }

    public int randomPlayer(Eventos evento) throws EventoInvalidoException{
        return this.tatica.randomPlayer(evento);
    }

    public boolean temTatica(){
        return tatica!=null;
    }
}