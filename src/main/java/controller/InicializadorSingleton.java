package controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class InicializadorSingleton {

    @EJB
    private TareasProgramadasBL tareasProgramadas;

    @PostConstruct
    private void postConstruct(){
        //try {
        System.out.println(
                "          __^__                                      __^__\n" +
                "         ( ___ )------------------------------------( ___ )\n" +
                "          | / |                                      | / |\n" +
                "          | / |          ESTO ES EL TUTANKA ;)       | / |\n" +
                "          |___|                                      |___|\n" +
                "         (_____)------------------------------------(_____) ");
        try{
           tareasProgramadas.execute("init");
        } catch (Exception e){
            System.out.println("Imposible ejecutar la tarea programada init " + e.getMessage());
            e.printStackTrace();
        }
    }
}
