package FootballManager.Exceptions;

public class JogadorInexistenteException extends Exception{
    public JogadorInexistenteException(){
        super();
    }
    public JogadorInexistenteException(String str){
        super(str);
    }
}