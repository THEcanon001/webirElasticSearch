package controller;

import ejb.WebirEJBBean;
import entity.ProgramedTask;

import javax.annotation.Resource;
import javax.ejb.*;

/**
 * Created with IntelliJ IDEA.
 * User: christian
 * Date: 9/10/2019
 * Time: 08:42 AM
 */

@LocalBean
@Stateless
public class ProgramedTaskBL {
    @Resource
    private TimerService timerService;

    @EJB
    WebirEJBBean webirEJBBean;

    private final static ProgramedTask tp = new ProgramedTask(0, "update", "1", "1", "1", "1");

    public void init(){
        try {
            webirEJBBean.init();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

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
                case "update":
                    try {
                        System.out.println("Se ejecuta tarea de actualizacion");
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
