package controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class InicializadorSingleton {

    @EJB
    private ProgramedTaskBL tareasProgramadas;

    @PostConstruct
    private void postConstruct(){
        System.out.println(
                "          __^__                                      __^__\n" +
                "         ( ___ )------------------------------------( ___ )\n" +
                "          | / |                                      | / |\n" +
                "          | / |         VAMOS AL TUTANKA ;)          | / |\n" +
                "          |___|                                      |___|\n" +
                "         (_____)------------------------------------(_____) ");

        try{
            tareasProgramadas.execute("init");
        } catch (Exception e){
            System.out.println("Imposible ejecutar inicializacion de datos " + e.getMessage());
            e.printStackTrace();
        }
    }
}
