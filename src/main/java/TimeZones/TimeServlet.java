package TimeZones;


import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@WebServlet("/time")
public class TimeServlet extends HttpServlet {

    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {

        templateEngine = new TemplateEngine();
        FileTemplateResolver fileTemplateResolver = new FileTemplateResolver();
        fileTemplateResolver.setPrefix("/Users/Anton/Downloads/HomeWorkServlets11/src/main/resources/frontend/");
        fileTemplateResolver.setSuffix(".html");
        fileTemplateResolver.setTemplateMode("HTML5");
        fileTemplateResolver.setOrder(templateEngine.getTemplateResolvers().size());
        fileTemplateResolver.setCacheable(false);

        templateEngine.addTemplateResolver(fileTemplateResolver);
    }

    public static String showTimeNow(String timezone) {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime;

        if (timezone == null || timezone.isEmpty()) {
            zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("UTC"));
            timezone = "It's your local time !";
        } else {
            try {

                int getTimeZone = Integer.parseInt(timezone.replace("UTC", "").trim());
                zonedDateTime = ZonedDateTime.now(ZoneOffset.ofHours(getTimeZone));

            } catch (NumberFormatException e) {
                return "Invalid timezone format";
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        return zonedDateTime.format(formatter) + " " + timezone;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String timezone = req.getParameter("timezone");
        resp.setCharacterEncoding("UTF-8");

        Context context = new Context(

                req.getLocale(),
                Map.of("time", showTimeNow(timezone))

        );

        resp.setContentType("text/html");
        templateEngine.process("showtime", context, resp.getWriter());
        resp.getWriter().close();

    }
}

