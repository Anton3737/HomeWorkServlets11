package TimeZones;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(value = "/times")
public class TimeZoneFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String timezone = req.getParameter("timezone");

        if (timezone == null || !validateTimezone(timezone)) {
            res.setStatus(400);
            res.setContentType("text/html");
            PrintWriter out = res.getWriter();
            out.println("<h1>Invalid timezone</h1>");
        } else {
            chain.doFilter(req, res);
        }
    }

    private boolean validateTimezone(String timezone) {
        if (timezone == null || timezone.length() != 6) return false;

        String prefix = timezone.substring(0, 3);
        char sign = timezone.charAt(3);
        String hoursStr = timezone.substring(4);

        if (!"UTC".equals(prefix)) return false;
        if (sign != '+' && sign != '-') return false;

        try {
            int hours = Integer.parseInt(hoursStr);
            return hours >= 0 && hours <= 24;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
