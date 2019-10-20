package controller;

import entity.ProgramedTask;

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
            //inicializo la carga de vehiculos y luego seteo la ejecucion de la tarea cada 1 hora.
            tareasProgramadas.setTp(new ProgramedTask(0, "init", "*", "*", "*", "*"));
            tareasProgramadas.execute("init");
            tareasProgramadas.setTp(new ProgramedTask(0, "init", "*", "1", "1", "1"));
        } catch (Exception e){
            System.out.println("Imposible ejecutar inicializacion de datos " + e.getMessage());
            e.printStackTrace();
        }
    }
}
