package solution;

import solution.ground.GroundFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by ecosim on 4/9/17.
 */
@WebServlet(
        urlPatterns = "/play",
        loadOnStartup = 0
)
public class IndexServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        config.getServletContext().addListener(SessionListener.class);
        super.init(config);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.err.println("Session post: " + req.getSession().getId());
        resp.getWriter().println(req.getParameterMap().entrySet().stream()
                .map(e -> e.getKey() + " -> " + Arrays.toString(e.getValue()))
                .collect(Collectors.joining("\n")));

        boolean finiteness = Boolean.parseBoolean(req.getParameter("finiteness"));
        boolean cyclicity = Boolean.parseBoolean(req.getParameter("cyclicity"));
        int sizeX = Integer.parseInt(req.getParameter("field-width"));
        int sizeY = Integer.parseInt(req.getParameter("field-height"));
        long seed = Long.parseLong(req.getParameter("seed"));
        boolean needWindowing = req.getParameter("windowing") != null;



        req.setAttribute("finiteness", finiteness);
        req.setAttribute("cyclicity", cyclicity);
        req.setAttribute("field-width", sizeX);
        req.setAttribute("field-height", sizeY);
        req.setAttribute("seed", seed);

        req.setAttribute("tick", 0);
        //req.setAttribute("");

        req.getSession().setAttribute("ground",
                GroundFactory.getInstance().getGround(finiteness, cyclicity, sizeX, sizeY, seed));
        req.getSession().setMaxInactiveInterval(60);

        getServletContext().getRequestDispatcher("/play.jsp").forward(req, resp);
    }
}
