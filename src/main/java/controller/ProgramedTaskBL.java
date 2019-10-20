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
    private ProgramedTask tp = new ProgramedTask(0, "init", "*", "*", "2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40,42,44,46,48,50,52,54,56,58", "*");

    public void execute(String tarea) throws Exception {
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setPersistent(false);
        timerConfig.setInfo(tarea);

        ScheduleExpression scheduleExpression = new ScheduleExpression();
        scheduleExpression.dayOfMonth(tp.getDia()).hour(tp.getHora()).minute(tp.getMinuto()).second(tp.getSegundo());
        tp = new ProgramedTask(0, "init", "*", "1", "1", "1");
        Timer timer = this.timerService.createCalendarTimer(scheduleExpression, timerConfig);
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

            if ("init".equals(splitTarea[0])) {
                try {
                    System.out.println("Se ejecuta tarea de inicializacion");
                    webirEJBBean.init();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new Exception("Error al procesar la tarea.");
                }
            } else {
                timer.cancel();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage(), ex);
        }
    }
}
