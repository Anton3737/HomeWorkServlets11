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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@WebServlet("/times")
public class TimeServletCookiess extends HttpServlet {
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String timezoneParam = req.getParameter("timezone");
        int timezoneOffset = getTimezoneOffset(timezoneParam);

        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now().plusHours(timezoneOffset);

        Context context = new Context(req.getLocale(), Map.of("currentDate", currentDate, "currentTime", currentTime, "timezone", timezoneParam));
        templateEngine.process("showtime", context, resp.getWriter());
    }

    private int getTimezoneOffset(String timezoneParam) {
        int timezoneOffset = 0;
        if (timezoneParam != null && !timezoneParam.isEmpty()) {
            try {
                char sign = timezoneParam.charAt(3);
                int hours = Integer.parseInt(timezoneParam.substring(4));
                timezoneOffset = (sign == '+') ? hours : -hours;
            } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            }
        }
        return timezoneOffset;
    }
}
