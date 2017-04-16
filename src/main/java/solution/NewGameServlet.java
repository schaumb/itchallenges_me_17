package solution;

import solution.ground.Ground;
import solution.ground.GroundFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ecosim on 4/10/17.
 */
@WebServlet(
        urlPatterns = "/newGame",
        loadOnStartup = 0
)
public class NewGameServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        GroundFactory.getInstance().removeGround((Ground) req.getSession().getAttribute("ground"));
        req.getSession().removeAttribute("ground");
        getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
    }
}
