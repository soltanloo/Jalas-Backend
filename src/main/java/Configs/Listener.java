package Configs;

import DataManagers.DataManager;
import Services.JWTService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener()
public class Listener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener, ServletRequestListener {

    private ScheduledExecutorService scheduler;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD'T'HH:mm:ss");

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Starting up!");
        try {
            DataManager.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new PeriodRoomCheck(), 1, 1, TimeUnit.MINUTES);
        scheduler.scheduleAtFixedRate(new PeriodPollDeadlineCheck(), 1, 1, TimeUnit.MINUTES);
        System.out.println(sdf.format(new Timestamp(System.currentTimeMillis())));
    }

    @Override
    public void requestInitialized(ServletRequestEvent event) {
        HttpServletRequest req = (HttpServletRequest) event.getServletRequest();
        String userId = JWTService.decodeUserIdJWT(req.getHeader("user-token"));
        if (userId == null)
            req.setAttribute("userId", "");
        else
            req.setAttribute("userId", userId);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        scheduler.shutdownNow();
    }

}
