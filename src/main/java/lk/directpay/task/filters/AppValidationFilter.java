package lk.directpay.task.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lk.directpay.task.entity.AppData;
import lk.directpay.task.model.DefaultResponse;
import lk.directpay.task.repository.AppDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class AppValidationFilter extends OncePerRequestFilter {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Autowired
    AppDataRepository appDataRepository;

    @Value("${env.bypass_app_validation}")
    private boolean bypassValidation;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getRequestURI().contains("api/")) {
            String signature = request.getHeader("Signature");
            String buildNumber = request.getHeader("App-Version");
            String platform = request.getHeader("Platform");
            if (platform != null) {
                platform = platform.toUpperCase();
            } else {
                platform = "Unknown";
            }

            LOGGER.log(Level.INFO,
                    "app-version: " + buildNumber + " platform:" + platform + " signature: " + signature + " " + request.getRequestURI());
            // check app hash
            AppData appData = buildNumber != null || signature != null ? appDataRepository.findAppDataByPlatformAndBuildNumberAndSignature(platform,
                    Integer.parseInt(buildNumber), signature) : null;
            if (appData != null) {
                LOGGER.log(Level.INFO, "valid app");
                filterChain.doFilter(request, response);

            } else if (bypassValidation) {
                LOGGER.log(Level.SEVERE, "invalid app signature. *** validation bypassed ***");
                filterChain.doFilter(request, response);
            } else {
                LOGGER.log(Level.SEVERE, "invalid app signature");
                DefaultResponse defaultResponse = DefaultResponse.getInstance(500, "App validation failed", "Please update the app and retry. If the problem persists please contact customer support",
                        null);

                PrintWriter writer = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                writer.print(convertObjectToJson(defaultResponse));
            }
        } else {
            filterChain.doFilter(request, response);
        }

    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

}
