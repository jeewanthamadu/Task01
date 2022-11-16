package lk.directpay.task.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;

import lk.directpay.task.entity.AppUser;
import lk.directpay.task.model.DefaultResponse;
import lk.directpay.task.repository.UserRepository;
import lk.directpay.task.services.AuthUserDetailsService;
import lk.directpay.task.utility.JWTUtility;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {
    @Autowired
    AuthUserDetailsService userService;

    @Autowired
    JWTUtility utility;

    @Autowired
    UserRepository userRepository;

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static final String MDC_UID_KEY = "uid";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authorization = request.getHeader("Authorization");
            String token = null;
            String userName = null;

            if (null != authorization && authorization.startsWith("Bearer ")) {
                token = authorization.substring(7);
                userName = utility.getUsernameFromToken(token);
            }

            if (null != userName && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userService.loadUserByUsername(userName);

                if (utility.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                            = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());

                    usernamePasswordAuthenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    List<AppUser> appUser = userRepository.findByUsername(userName);
                    if (appUser.size() == 1) {
                        String deviceId = request.getHeader("Device-Id");
                        if (deviceId != null) {
                            if (deviceId.equals(appUser.get(0).getDeviceId())) {
                                request.setAttribute("user", appUser.get(0));

                                MDC.put(MDC_UID_KEY, appUser.get(0).getUsername());
                            } else {
                                throw new JwtException("Session timed out");
                            }
                        } else {
                            LOGGER.log(Level.SEVERE, "Device-ID header not found.");
                            throw new JwtException("Bad Request!.");
                        }
                    }
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

            LOGGER.log(Level.SEVERE, "Request Filter: " + request.getContextPath() + " User: " + (request.getAttribute("user") != null ? ((AppUser) request.getAttribute("user")).getUsername() : "Public user"));

            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            LOGGER.log(Level.SEVERE, "JWT Expired...Session timed out");
            DefaultResponse defaultResponse = new DefaultResponse(401, "Failed", "Session timed out");

            PrintWriter writer = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            writer.print(convertObjectToJson(defaultResponse));
        } finally {
            MDC.remove(MDC_UID_KEY);
        }
    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    public DefaultResponse createErrorResponse() {
        LOGGER.log(Level.SEVERE, "JWT Expired...");
        return new DefaultResponse(401, "Failed", "JWT token Expired", null);
    }
}
