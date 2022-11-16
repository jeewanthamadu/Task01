package lk.directpay.task.filters;

import com.fasterxml.jackson.databind.ObjectMapper;


import lk.directpay.task.entity.AppUserActivity;
import lk.directpay.task.services.ActivityLogService;
import org.apache.commons.io.output.TeeOutputStream;
import org.json.JSONObject;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RequestDataLogFilter extends OncePerRequestFilter {

    public static final String MDC_UUID_TOKEN_KEY = "req-id";

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private final ActivityLogService activityLogService;
    private final ObjectMapper objectMapper;

    public RequestDataLogFilter(ActivityLogService activityLogService, ObjectMapper objectMapper) {
        this.activityLogService = activityLogService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest httpServletRequest,
            final HttpServletResponse httpServletResponse, final FilterChain chain)
            throws IOException, ServletException {
        try {
            final String token = UUID.randomUUID().toString().replace("-", "");
            MDC.put(MDC_UUID_TOKEN_KEY, token);

            Map<String, String> requestMap = this.getTypesafeRequestMap(httpServletRequest);

            boolean allowSkippingRequest = skipRequestLogging(httpServletRequest);
            if (allowSkippingRequest) {
                chain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }

            BufferedRequestWrapper bufferedRequest = new BufferedRequestWrapper(httpServletRequest);
            BufferedResponseWrapper bufferedResponse = new BufferedResponseWrapper(httpServletResponse);

            String requestBody = "";
            try {
                JSONObject requestDataObject = new JSONObject(bufferedRequest.getRequestBody());
                requestDataObject.remove("password");
                requestDataObject.remove("acc_no");
                requestDataObject.remove("pin");
                requestBody = requestDataObject.toString();
            } catch (Exception e) {

            }
            final StringBuilder logMessage = new StringBuilder("[HTTP METHOD:").append(httpServletRequest.getMethod())
                    .append("] [PATH:").append(httpServletRequest.getServletPath()).append("] [REQUEST BODY:")
                    .append(requestBody).append("] [REMOTE ADDRESS:")
                    .append(httpServletRequest.getRemoteAddr()).append("]");

            AppUserActivity appUserActivity = AppUserActivity.builder()
                    .requestId(MDC.get("req-id"))
                    .ipAddress(httpServletRequest.getRemoteAddr())
                    .apiEndpoint(httpServletRequest.getServletPath())
                    .action(activityLogService.getActionFromPath(httpServletRequest.getServletPath()))
                    .apiRequest(logMessage.toString())
                    .build();

            activityLogService.saveReqLogActivity(appUserActivity);

            //create log reference
            String reference = createReference(appUserActivity);
            appUserActivity.setUserReference(reference);
            activityLogService.saveChanges(appUserActivity);

            //set log reference to request scope
            RequestContextHolder.currentRequestAttributes().setAttribute("activity_ref",
                    reference, RequestAttributes.SCOPE_REQUEST);

            LOGGER.log(Level.INFO, "request: " + logMessage);

            chain.doFilter(bufferedRequest, bufferedResponse);

            try {
                JSONObject responseObject = new JSONObject(bufferedResponse.getContent());
                //skip logging data object
                boolean allowSkippingResponse = skipResponseLogging(httpServletRequest);
                if (allowSkippingResponse) {
                    responseObject.remove("data");
                }
                if (responseObject.has("status")) {
                    appUserActivity.setApiStatusCode(responseObject.get("status").toString());
                    appUserActivity.setApiStatusDescription(responseObject.get("message").toString());
                    appUserActivity.setDescription(responseObject.get("message").toString());
                    appUserActivity.setStatus(responseObject.get("status").toString().equals("200") ? AppUserActivity.SUCCESS : AppUserActivity.FAILED);
                }

                try {
                    // responseObject.remove("data");
                    appUserActivity.setApiResponse(new ObjectMapper().writeValueAsString(responseObject.toMap()));
                    activityLogService.saveChanges(appUserActivity);
                    LOGGER.log(Level.INFO, "response: " + responseObject.toString());
                } catch (Exception e) {
                    LOGGER.log(Level.INFO, "response: " + bufferedResponse.getContent());
                }

            } catch (Exception e) {
            }

        } finally {
            MDC.remove(MDC_UUID_TOKEN_KEY);
        }
    }

    private String createReference(AppUserActivity appUserActivity){
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmss");
        return ft.format(dNow) + appUserActivity.getId();
    }

    private boolean skipRequestLogging(HttpServletRequest httpServletRequest) {
        //Add request path if you want to bypass writing the request body to log
        String[] regs = {
                "/resource/(.*?)",
//                "/upload/images",
//                "/change/nic/img",
//                "/change/profile/image"

        };
        Matcher matcher;
        for (String pathExpr: regs) {
            matcher = Pattern.compile(pathExpr).matcher(httpServletRequest.getServletPath());
            if (matcher.find()) {
                LOGGER.log(Level.INFO, "Request: PATH: " + httpServletRequest.getServletPath());
                return true;
            }
        }
        return false;
    }

    private boolean skipResponseLogging(HttpServletRequest httpServletRequest) {
        //Add request path if you want to bypass writing the response body to log
        String[] regs = {
                "/api/city/list",
                "/config/termsAndConditions",
                "/api/account/statement",
                "/api/account/statement/pdf",
                "/api/account/all",
                "/api/location/list",
                "/api/transaction/all",
                "/api/biller/recent/transactions",
                "/api/biller/all",
                "/api/promotion/all",
                "/api/bank/all/ceft",
                "/api/card/statement/list",
                "/api/card/statement/details"
        };
        Matcher matcher;
        for (String pathExpr: regs) {
            matcher = Pattern.compile(pathExpr).matcher(httpServletRequest.getServletPath());
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }

    private Map<String, String> getTypesafeRequestMap(HttpServletRequest request) {
        Map<String, String> typesafeRequestMap = new HashMap<String, String>();
        Enumeration<?> requestParamNames = request.getParameterNames();
        while (requestParamNames.hasMoreElements()) {
            String requestParamName = (String) requestParamNames.nextElement();
            String requestParamValue;
            if (requestParamName.equalsIgnoreCase("password")) {
                requestParamValue = "********";
            } else {
                requestParamValue = request.getParameter(requestParamName);
            }
            typesafeRequestMap.put(requestParamName, requestParamValue);
        }
        return typesafeRequestMap;
    }

    @Override
    public void destroy() {
    }

    private static final class BufferedRequestWrapper extends HttpServletRequestWrapper {

        private ByteArrayInputStream bais = null;
        private ByteArrayOutputStream baos = null;
        private BufferedServletInputStream bsis = null;
        private byte[] buffer = null;

        public BufferedRequestWrapper(HttpServletRequest req) throws IOException {
            super(req);
            // Read InputStream and store its content in a buffer.
            InputStream is = req.getInputStream();
            this.baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int read;
            while ((read = is.read(buf)) > 0) {
                this.baos.write(buf, 0, read);
            }
            this.buffer = this.baos.toByteArray();
        }

        @Override
        public ServletInputStream getInputStream() {
            this.bais = new ByteArrayInputStream(this.buffer);
            this.bsis = new BufferedServletInputStream(this.bais);
            return this.bsis;
        }

        String getRequestBody() throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.getInputStream()));
            String line = null;
            StringBuilder inputBuffer = new StringBuilder();
            do {
                line = reader.readLine();
                if (null != line) {
                    inputBuffer.append(line.trim());
                }
            } while (line != null);
            reader.close();
            return inputBuffer.toString().trim();
        }

    }

    private static final class BufferedServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream bais;

        public BufferedServletInputStream(ByteArrayInputStream bais) {
            this.bais = bais;
        }

        @Override
        public int available() {
            return this.bais.available();
        }

        @Override
        public int read() {
            return this.bais.read();
        }

        @Override
        public int read(byte[] buf, int off, int len) {
            return this.bais.read(buf, off, len);
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }
    }

    public class TeeServletOutputStream extends ServletOutputStream {

        private final TeeOutputStream targetStream;

        public TeeServletOutputStream(OutputStream one, OutputStream two) {
            targetStream = new TeeOutputStream(one, two);
        }

        @Override
        public void write(int arg0) throws IOException {
            this.targetStream.write(arg0);
        }

        public void flush() throws IOException {
            super.flush();
            this.targetStream.flush();
        }

        public void close() throws IOException {
            super.close();
            this.targetStream.close();
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }
    }

    public class BufferedResponseWrapper implements HttpServletResponse {

        HttpServletResponse original;
        TeeServletOutputStream tee;
        ByteArrayOutputStream bos;

        public BufferedResponseWrapper(HttpServletResponse response) {
            original = response;
        }

        public String getContent() {
            return bos.toString();
        }

        public PrintWriter getWriter() throws IOException {
            return original.getWriter();
        }

        public ServletOutputStream getOutputStream() throws IOException {
            if (tee == null) {
                bos = new ByteArrayOutputStream();
                tee = new TeeServletOutputStream(original.getOutputStream(), bos);
            }
            return tee;

        }

        @Override
        public String getCharacterEncoding() {
            return original.getCharacterEncoding();
        }

        @Override
        public String getContentType() {
            return original.getContentType();
        }

        @Override
        public void setCharacterEncoding(String charset) {
            original.setCharacterEncoding(charset);
        }

        @Override
        public void setContentLength(int len) {
            original.setContentLength(len);
        }

        @Override
        public void setContentLengthLong(long l) {
            original.setContentLengthLong(l);
        }

        @Override
        public void setContentType(String type) {
            original.setContentType(type);
        }

        @Override
        public void setBufferSize(int size) {
            original.setBufferSize(size);
        }

        @Override
        public int getBufferSize() {
            return original.getBufferSize();
        }

        @Override
        public void flushBuffer() throws IOException {
            tee.flush();
        }

        @Override
        public void resetBuffer() {
            original.resetBuffer();
        }

        @Override
        public boolean isCommitted() {
            return original.isCommitted();
        }

        @Override
        public void reset() {
            original.reset();
        }

        @Override
        public void setLocale(Locale loc) {
            original.setLocale(loc);
        }

        @Override
        public Locale getLocale() {
            return original.getLocale();
        }

        @Override
        public void addCookie(Cookie cookie) {
            original.addCookie(cookie);
        }

        @Override
        public boolean containsHeader(String name) {
            return original.containsHeader(name);
        }

        @Override
        public String encodeURL(String url) {
            return original.encodeURL(url);
        }

        @Override
        public String encodeRedirectURL(String url) {
            return original.encodeRedirectURL(url);
        }

        @SuppressWarnings("deprecation")
        @Override
        public String encodeUrl(String url) {
            return original.encodeUrl(url);
        }

        @SuppressWarnings("deprecation")
        @Override
        public String encodeRedirectUrl(String url) {
            return original.encodeRedirectUrl(url);
        }

        @Override
        public void sendError(int sc, String msg) throws IOException {
            original.sendError(sc, msg);
        }

        @Override
        public void sendError(int sc) throws IOException {
            original.sendError(sc);
        }

        @Override
        public void sendRedirect(String location) throws IOException {
            original.sendRedirect(location);
        }

        @Override
        public void setDateHeader(String name, long date) {
            original.setDateHeader(name, date);
        }

        @Override
        public void addDateHeader(String name, long date) {
            original.addDateHeader(name, date);
        }

        @Override
        public void setHeader(String name, String value) {
            original.setHeader(name, value);
        }

        @Override
        public void addHeader(String name, String value) {
            original.addHeader(name, value);
        }

        @Override
        public void setIntHeader(String name, int value) {
            original.setIntHeader(name, value);
        }

        @Override
        public void addIntHeader(String name, int value) {
            original.addIntHeader(name, value);
        }

        @Override
        public void setStatus(int sc) {
            original.setStatus(sc);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void setStatus(int sc, String sm) {
            original.setStatus(sc, sm);
        }

        @Override
        public String getHeader(String arg0) {
            return original.getHeader(arg0);
        }

        @Override
        public Collection<String> getHeaderNames() {
            return original.getHeaderNames();
        }

        @Override
        public Collection<String> getHeaders(String arg0) {
            return original.getHeaders(arg0);
        }

        @Override
        public int getStatus() {
            return original.getStatus();
        }

    }
}
