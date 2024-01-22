package com.et.security.jwt;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CustomerMaskingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;

            if ((req.getServletPath().startsWith("/subscriptions/cp/api"))
                || (req.getServletPath().startsWith("/subscriptions/bb/api"))) {

                ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response);

                chain.doFilter(request, responseWrapper);

                String responseContent = new String(responseWrapper.getDataStream());


                try {

                    JSONObject jsonObject = new JSONObject(responseContent);

                    if (jsonObject.has("customer")) {
                        JSONObject customer = jsonObject.getJSONObject("customer");
                        customer.remove("displayName");
                        customer.remove("email");
                        customer.remove("firstName");
                        customer.remove("lastName");
                        customer.remove("phone");

                        responseContent = jsonObject.toString();
                    }

                    byte[] responseToSend = responseContent.getBytes();

                    response.getOutputStream().write(responseToSend);
                } catch (Exception ex) {
                    byte[] responseToSend = responseContent.getBytes();
                    response.getOutputStream().write(responseToSend);
                }

            } else {
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }

    public class FilterServletOutputStream extends ServletOutputStream {

        DataOutputStream output;

        public FilterServletOutputStream(OutputStream output) {
            this.output = new DataOutputStream(output);
        }

        @Override
        public void write(int arg0) throws IOException {
            output.write(arg0);
        }

        @Override
        public void write(byte[] arg0, int arg1, int arg2) throws IOException {
            output.write(arg0, arg1, arg2);
        }

        @Override
        public void write(byte[] arg0) throws IOException {
            output.write(arg0);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }
    }

    public class ResponseWrapper extends HttpServletResponseWrapper {

        ByteArrayOutputStream output;
        FilterServletOutputStream filterOutput;
        HttpResponseStatus status = HttpResponseStatus.OK;

        public ResponseWrapper(HttpServletResponse response) {
            super(response);
            output = new ByteArrayOutputStream();
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            if (filterOutput == null) {
                filterOutput = new FilterServletOutputStream(output);
            }
            return filterOutput;
        }

        public byte[] getDataStream() {
            return output.toByteArray();
        }
    }
}
