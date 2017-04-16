package solution;

import solution.ground.Ground;
import solution.ground.GroundFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("tick") != null) {
            if (req.getParameter("nextX") != null) {
                req.getSession().setAttribute("tick", 1 + (Integer) req.getSession().getAttribute("tick"));
            }

            if (req.getParameter("prevX") != null) {
                req.getSession().setAttribute("tick", Math.max(0, ((Integer) req.getSession().getAttribute("tick")) - 1));
            }

            if (req.getParameter("zeroX") != null) {
                req.getSession().setAttribute("tick", 0);
            }
        }
        Boolean windowing = (Boolean) req.getSession().getAttribute("windowing");
        if (windowing != null && windowing) {
            if (req.getSession().getAttribute("showtop") != null) {
                if (req.getParameter("upB") != null) {
                    req.getSession().setAttribute("showtop", ((Integer) req.getSession().getAttribute("showtop")) - 1);
                }

                if (req.getParameter("downB") != null) {
                    req.getSession().setAttribute("showtop", 1 + (Integer) req.getSession().getAttribute("showtop"));
                }
            }
            if (req.getSession().getAttribute("showleft") != null) {
                if (req.getParameter("leftB") != null) {
                    req.getSession().setAttribute("showleft", ((Integer) req.getSession().getAttribute("showleft")) - 1);
                }

                if (req.getParameter("rightB") != null) {
                    req.getSession().setAttribute("showleft", 1 + (Integer) req.getSession().getAttribute("showleft"));
                }
            }
            if (req.getSession().getAttribute("showwidth") != null &&
                    req.getSession().getAttribute("showheight") != null) {
                if (req.getParameter("minusU") != null) {
                    req.getSession().setAttribute("showwidth",
                            Math.max(0, ((Integer) req.getSession().getAttribute("showwidth")) - 1));
                    req.getSession().setAttribute("showheight",
                            Math.max(0, ((Integer) req.getSession().getAttribute("showheight")) - 1));
                }

                if (req.getParameter("plusU") != null) {
                    req.getSession().setAttribute("showwidth",
                            Math.max(0, ((Integer) req.getSession().getAttribute("showwidth")) + 1));
                    req.getSession().setAttribute("showheight",
                            Math.max(0, ((Integer) req.getSession().getAttribute("showheight")) + 1));
                }
            }
        } else if (windowing != null) {
            if (req.getParameter("windowOn") != null) {
                req.getSession().setAttribute("windowing", true);
            }
        }
        Boolean endable = (Boolean) req.getSession().getAttribute("endable");
        if (endable != null && endable) {
            if (req.getParameter("endX") != null && req.getSession().getAttribute("ground") != null) {
                Ground.End end = ((Ground) req.getSession().getAttribute("ground")).tryToFindEnd();
                if (end.getFrom() != end.getTo()) {
                    req.setAttribute("message", "The end is from " + end.getFrom() + " tick to " + end.getTo() + " tick, and repeat itself");
                }
                req.getSession().setAttribute("tick", end.getTo());
            }
        }
        if (req.getParameter("playpause") != null) {
            Boolean playFW = (Boolean) req.getSession().getAttribute("playFW");
            if (playFW != null) {
                req.getSession().setAttribute("playFW", !playFW);
            }
        }
        req.getRequestDispatcher("/play.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        boolean finiteness = req.getParameter("finiteness").equals("finite");
        boolean cyclicity = req.getParameter("cyclicity").equals("cyclic");
        int sizeX = Integer.parseInt(req.getParameter("field-width"));
        int sizeY = Integer.parseInt(req.getParameter("field-height"));
        long seed = Long.parseLong(req.getParameter("seed"));
        boolean needWindowing = req.getParameter("windowing") == null || req.getParameter("windowing").equals("windowing");
        int left = Integer.parseInt(req.getParameter("show-left"));
        int top = Integer.parseInt(req.getParameter("show-top"));
        int width = Integer.parseInt(req.getParameter("show-width"));
        int height = Integer.parseInt(req.getParameter("show-height"));

        req.getSession().setAttribute("windowing", needWindowing);
        req.getSession().setAttribute("showleft", needWindowing ? left : 0);
        req.getSession().setAttribute("showtop", needWindowing ? top : 0);
        req.getSession().setAttribute("showwidth", needWindowing ? width : sizeX);
        req.getSession().setAttribute("showheight", needWindowing ? height : sizeY);
        req.getSession().setAttribute("endable", finiteness);
        req.getSession().setAttribute("playFW", false);

        req.getSession().setAttribute("tick", 0);

        req.getSession().setAttribute("ground",
                GroundFactory.getInstance().getGround(finiteness, cyclicity, sizeX, sizeY, seed));
        req.getSession().setMaxInactiveInterval(3600);
        getServletContext().getRequestDispatcher("/play.jsp").forward(req, resp);
    }
}
