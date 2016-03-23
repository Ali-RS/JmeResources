package org.jmonkey.external.discourse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.server.ServerResponseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jayfella
 */

public class DiscourseApiClient {

    public DiscourseApiClient() {

    }

    public DiscourseLoginResult login(String username, String password) throws IOException, ServerResponseException {

        HttpClientContext context = new HttpClientContext();

        String csrf;

        try(CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCookieStore(new BasicCookieStore())
                .setUserAgent(JmeResourceWebsite.getInstance().getConfiguration().getGeneralConfig().getUserAgent())
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build()) {

            HttpUriRequest request = RequestBuilder.get()
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .setUri(JmeResourceWebsite.getInstance().getConfiguration().getDiscourseConfig().getBaseUrl() + "/session/csrf?_=" + System.currentTimeMillis())
                    .build();

            try(CloseableHttpResponse httpResponse = httpClient.execute(request, context)) {

                int statusCode = httpResponse.getStatusLine().getStatusCode();

                if (statusCode >= 400) {
                    throw new ServerResponseException(request.getURI().toString(), statusCode, httpResponse.getStatusLine().getReasonPhrase());
                }

                HttpEntity entity = httpResponse.getEntity();
                String response = EntityUtils.toString(entity);
                EntityUtils.consume(entity);

                ObjectMapper objectMapper = new ObjectMapper();

                Map<String, String> csrfData = objectMapper.readValue(response, objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, String.class));

                if (csrfData.get("csrf") == null) {
                    throw new ServerResponseException(request.getURI().toString(), statusCode, httpResponse.getStatusLine().getReasonPhrase());
                }

                csrf = csrfData.get("csrf");
            }

            request = RequestBuilder.post()
                    .setUri(JmeResourceWebsite.getInstance().getConfiguration().getDiscourseConfig().getBaseUrl() + "/session")
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .addHeader("X-CSRF-Token", csrf)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addParameter("login", username)
                    .addParameter("password", password)
                    .build();

            try(CloseableHttpResponse httpResponse = httpClient.execute(request, context)) {

                int statusCode = httpResponse.getStatusLine().getStatusCode();

                if (statusCode >= 400) {
                    throw new ServerResponseException(request.getURI().toString(), statusCode, httpResponse.getStatusLine().getReasonPhrase());
                }

                HttpEntity entity = httpResponse.getEntity();
                String response = EntityUtils.toString(entity);
                EntityUtils.consume(entity);

                ObjectMapper objectMapper = new ObjectMapper();

                DiscourseLoginResult loginResult = objectMapper.readValue(response, DiscourseLoginResult.class);

                String forumSession = context.getCookieStore().getCookies().stream()
                        .filter(cookie -> cookie.getName().equals("_forum_session"))
                        .map(Cookie::getValue)
                        .findFirst()
                        .orElse("");

                loginResult.setSession(forumSession);

                return loginResult;
            }
        }
    }

}
