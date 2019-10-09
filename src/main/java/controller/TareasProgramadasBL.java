package controller;

import ejb.WebirEJBBean;
import entity.ProgramedTask;

import javax.annotation.Resource;
import javax.ejb.*;

/**
 * Created with IntelliJ IDEA.
 * User: edu
 * Date: 26/12/12
 * Time: 02:35 PM
 */

@LocalBean
@Stateless
public class TareasProgramadasBL {
    @Resource
    private TimerService timerService;

    @EJB
    WebirEJBBean webirEJBBean;

    private final static ProgramedTask tp = new ProgramedTask(0, "init", "*", "*", "*", "0,5,10,15,20,25,30,35,40,45,50,55");

    public void execute(String tarea) throws Exception {
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setPersistent(false);
        timerConfig.setInfo(tarea);

        if (tp != null) {
            ScheduleExpression scheduleExpression = new ScheduleExpression();
            scheduleExpression.dayOfMonth(tp.getDia()).hour(tp.getHora()).minute(tp.getMinuto()).second(tp.getSegundo());

            Timer timer = this.timerService.createCalendarTimer(scheduleExpression, timerConfig);
        } else {
            throw new Exception("ERROR al configurar tarea programada " + tarea + ". No existe en la base de datos");
        }
    }

    @Timeout
    private void onTimeOut(Timer timer) throws Exception {
        try {
            String tarea = (String) timer.getInfo();
            String splitTarea[] = tarea.split(";");
            ScheduleExpression se = timer.getSchedule();

            if (!(tp.getDia().equals(se.getDayOfMonth()) && tp.getHora().equals(se.getHour()) && tp.getMinuto().equals(se.getMinute()) && tp.getSegundo().equals(se.getSecond()))) {
                timer.cancel();
                TimerConfig timerConfig = new TimerConfig();
                timerConfig.setPersistent(false);
                timerConfig.setInfo(tarea);

                ScheduleExpression scheduleExpression = new ScheduleExpression();
                scheduleExpression.dayOfMonth(tp.getDia()).hour(tp.getHora()).minute(tp.getMinuto());

                Timer timerTemp = this.timerService.createCalendarTimer(scheduleExpression, timerConfig);
            }

            //        timer.cancel();
            switch (splitTarea[0]) {
                case "init":
                    try {
                        System.out.println("Se ejecuta tarea de inicializacion");
                        webirEJBBean.init();
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new Exception("Error al procesar la tarea.");
                    }
                    break;
                default:
                    timer.cancel();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage(), ex);
        }
    }
}
