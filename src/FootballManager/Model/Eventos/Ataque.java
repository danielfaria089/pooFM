package FootballManager.Model.Eventos;

import FootballManager.Model.Equipas.Equipa;
import FootballManager.Model.Exceptions.EventoInvalidoException;
import FootballManager.Model.Exceptions.JogadorInexistenteException;
import FootballManager.Model.Exceptions.TaticaInvalidaException;

import java.util.Random;

public class Ataque extends Eventos{

    public Ataque(){
        super();
    }

    @Override
    public boolean golo(Equipa Atacante, Equipa Defensora) throws TaticaInvalidaException {
        Random r=new Random();
        double chance = 0.2+((Atacante.ataque()-Defensora.defesa())/100.0)*r.nextGaussian();
        try{
            if(r.nextDouble()<chance){
                if(r.nextDouble()<Atacante.chanceCruzamento()){
                    Cruzamento cruzamento = new Cruzamento();
                    cruzamento.setMarcador(Atacante.getJogador(Atacante.randomPlayer(cruzamento)));
                    return cruzamento.golo(Atacante,Defensora);
                }else{
                    Remate remate = new Remate();
                    remate.setMarcador(Atacante.getJogador(Atacante.randomPlayer(remate)));
                    return remate.golo(Atacante,Defensora);
                }
            }
            else{
                if(r.nextDouble()<0.33){
                    if(r.nextBoolean()){
                        Canto canto = new Canto(r.nextBoolean());
                        canto.setMarcador(Atacante.getJogador(Atacante.randomPlayer(canto)));
                        return canto.golo(Defensora,Atacante);
                    }
                    else {
                        double distancia=25+25*r.nextGaussian();
                        if(distancia<16.5)distancia=20+3.5*r.nextGaussian();
                        Livre livre = new Livre((float) distancia);
                        livre.setMarcador(Atacante.getJogador(Atacante.randomPlayer(livre)));
                        return livre.golo(Defensora,Atacante);
                    }
                }
                else return false;
            }
        } catch (EventoInvalidoException | JogadorInexistenteException e) {
            e.printStackTrace();
            return false;
        }
    }


}
