package solution;

import solution.ground.Ground;
import solution.ground.GroundFactory;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by ecosim on 4/10/17.
 */
@WebListener
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        GroundFactory.getInstance()
                .removeGround((Ground) httpSessionEvent.getSession().getAttribute("ground"));
        httpSessionEvent.getSession().removeAttribute("ground");
    }
}
