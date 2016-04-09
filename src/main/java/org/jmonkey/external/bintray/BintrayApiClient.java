package org.jmonkey.external.bintray;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;
import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.database.asset.Asset;
import org.jmonkey.database.asset.AssetVersion;
import org.jmonkey.database.asset.PotentialAsset;
import org.jmonkey.database.configuration.BintrayConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author jayfella
 */

public class BintrayApiClient {

    private final String userAgent = JmeResourceWebsite.getInstance().getConfiguration().getGeneralConfig().getUserAgent();
    private final BintrayConfiguration config = JmeResourceWebsite.getInstance().getConfiguration().getBintrayConfig();

    public BintrayApiClient() {

    }
    
    private CloseableHttpClient createClient() {
        
        return HttpClients.custom()
                .setDefaultCookieStore(new BasicCookieStore())
                .setUserAgent(userAgent)
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
    }
    
    private CloseableHttpClient createAuthenticatedClient() {
        
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(config.getUser(), config.getApiKey()));
        
        return HttpClients.custom()
                .setDefaultCookieStore(new BasicCookieStore())
                .setUserAgent(userAgent)
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();
    }
    
    private String cleanQuery(final String query) {
        
        // @TODO: needs discussion.
        
        return query
                .replaceAll("&", "")
                .replaceAll("&amp;", "")
                .replaceAll("&#38;", "")
                .replaceAll("\\?", "")
                .replaceAll("&#63;", "");
    }
    
    public List<BintrayFile> search(final String query) throws IOException {
        
        try(CloseableHttpClient httpClient = this.createClient()) {
        
            HttpUriRequest request = RequestBuilder.get()
                    .setUri("https://api.bintray.com/search/packages/maven?q=*" + cleanQuery(query) + "*&owner=" + config.getSubject() + "&repo=" + config.getRepo())
                    .build();

            try (CloseableHttpResponse httpResponse = httpClient.execute(request)) {

                int statusCode = httpResponse.getStatusLine().getStatusCode();

                if (statusCode >= 400) {
                    // @TODO: provide a consumable error
                }

                HttpEntity entity = httpResponse.getEntity();
                String entityString = EntityUtils.toString(entity);
                EntityUtils.consume(entity);

                List<BintrayFile> results = JmeResourceWebsite.getObjectMapper().readValue(entityString, new TypeReference<List<BintrayFile>>(){});

                return results;
            }
        }
    }
    
    public BintrayFile get(final String packageName) throws IOException {
        
        try(CloseableHttpClient httpClient = this.createClient()) {
            
            HttpUriRequest request = RequestBuilder.get()
                    .setUri("https://api.bintray.com/packages/" + config.getSubject() + "/" + config.getRepo() + "/" + cleanQuery(packageName))
                    .build();
            
            try (CloseableHttpResponse httpResponse = httpClient.execute(request)) {
                
                int statusCode = httpResponse.getStatusLine().getStatusCode();

                if (statusCode >= 400) {
                    // @TODO: provide a consumable error
                }
                
                HttpEntity entity = httpResponse.getEntity();
                String entityString = EntityUtils.toString(entity);
                EntityUtils.consume(entity);

                BintrayFile result = JmeResourceWebsite.getObjectMapper().readValue(entityString, BintrayFile.class);

                return result;
            }
        }
    }

    public String getPackageReadme(String packageName) throws IOException {
        
        try (CloseableHttpClient httpClient = this.createClient()) {
            
            HttpUriRequest request = RequestBuilder.get()
                    .setUri("https://api.bintray.com/packages/" + config.getSubject() + "/" + config.getRepo() + "/" + this.cleanQuery(packageName) + "/readme")
                    .build();
            
            try(CloseableHttpResponse httpResponse = httpClient.execute(request)) {
                
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                
                if (statusCode >= 400) {
                    // @TODO: provide a consumable error
                }
                
                HttpEntity entity = httpResponse.getEntity();
                String entityString = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
                
                Map<Object, Object> result = JmeResourceWebsite.getObjectMapper().readValue(entityString, new TypeReference<Map<Object, Object>>(){});
                
                // https://bintray.com/docs/api/#_get_readme
                
                String noReadmeFound = "this package does not contain a readme.";
                
                if (result == null || result.isEmpty()) {
                    return noReadmeFound;
                }
                
                if (result.get("message") != null) {
                    return noReadmeFound;
                }
                
                if (result.get("bintray") != null) {

                    @SuppressWarnings("unchecked")
                    Map<String, String> bintrayData = (Map<String, String>) result.get("bintray");

                    if (bintrayData.get("content") == null) {
                        return noReadmeFound;
                    }
                    
                    return bintrayData.get("content");
                }
                
                if (result.containsKey("github")) {

                    @SuppressWarnings("unchecked")
                    Map<String, String> githubData = (Map<String, String>) result.get("github");
                    
                    if (githubData.get("github_repo") == null) {
                        return noReadmeFound;
                    }
                    
                    return "github readme.";
                }
                
                return noReadmeFound;
            }
        }
        
    }

    public boolean createPackage(PotentialAsset asset) throws IOException {

        try (CloseableHttpClient httpClient = this.createAuthenticatedClient()) {

            // @link https://bintray.com/docs/api/#_create_package

            List<String> licenses = new ArrayList<>();
            licenses.add(asset.getLicenseType());

            Map<String, Object> jsonData = new HashMap<>();
            jsonData.put("name", asset.getPackageName());
            jsonData.put("desc", "jMonkey something something...");
            jsonData.put("labels", "");
            jsonData.put("licenses", licenses);
            // jsonData.put("custom_licenses", "");
            jsonData.put("vcs_url", "http://www.jmonkeyengine.org");
            jsonData.put("website_url", "http://www.jmonkeyengine.org");
            jsonData.put("issue_tracker_url", "");
            jsonData.put("github_repo", "");
            jsonData.put("github_release_notes_file", "");
            jsonData.put("public_download_numbers", "true");
            // jsonData.put("public_stats", "false");

            String json = JmeResourceWebsite.getObjectMapper().writeValueAsString(jsonData);

            StringEntity requestEntity = new StringEntity(json, ContentType.APPLICATION_JSON);

            HttpUriRequest request = RequestBuilder.post()
                    .setUri("https://api.bintray.com/packages/" + config.getSubject() + "/" + config.getRepo())
                    .setEntity(requestEntity)
                    .build();

            try (CloseableHttpResponse httpResponse = httpClient.execute(request)) {

                if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                    return false;
                }

                return true;
            }
        }
    }

    public boolean createPackageVersion(Asset asset, AssetVersion assetVersion) throws IOException {

        try (CloseableHttpClient httpClient = this.createAuthenticatedClient()) {

            // @link https://bintray.com/docs/api/#_create_version

            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
            df.setTimeZone(tz);
            String nowAsISO = df.format(new Date());

            Map<String, Object> jsonData = new HashMap<>();
            jsonData.put("name", assetVersion.getVersion());
            jsonData.put("released", nowAsISO);
            jsonData.put("desc", "");
            jsonData.put("github_release_notes_file", "");
            jsonData.put("github_use_tag_release_notes", "false");
            jsonData.put("vcs_tag", "version");

            String json = JmeResourceWebsite.getObjectMapper().writeValueAsString(jsonData);

            StringEntity requestEntity = new StringEntity(json, ContentType.APPLICATION_JSON);

            HttpUriRequest request = RequestBuilder.post()
                    .setUri("https://api.bintray.com/packages/" + config.getSubject() + "/" + config.getRepo() + "/" + asset.getPackageName() + "/versions")
                    .setEntity(requestEntity)
                    .build();

            try (CloseableHttpResponse httpResponse = httpClient.execute(request)) {

                if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                    return false;
                }

                return true;
            }
        }
    }

    public boolean uploadPackage(PotentialAsset potentialAsset, Asset asset, AssetVersion assetVersion) throws IOException {

        try (CloseableHttpClient httpClient = this.createAuthenticatedClient()) {

            FileEntity fileEntity = new FileEntity(new File(potentialAsset.getAssetFile()));

            HttpUriRequest request = RequestBuilder.put()
                    .setUri("https://api.bintray.com/content/" + config.getSubject() + "/" + config.getRepo() + "/" + asset.getPackageName() + "/" + assetVersion.getVersion() + "/" + asset.getPackageName() + ".jar?publish=1")
                    .setEntity(fileEntity)
                    .build();

            try (CloseableHttpResponse httpResponse = httpClient.execute(request)) {

                if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                    return false;
                }

                return true;
            }

        }
    }

    public boolean createReadme(Asset asset, String readmeData) throws IOException {

        // @link https://bintray.com/docs/api/#url_package_readme

        try (CloseableHttpClient httpClient = this.createAuthenticatedClient()) {

            Map<String, Object> jsonData = new HashMap<>();
            Map<String, String> bintrayData = new HashMap<>();

            bintrayData.put("syntax", "markdown");
            bintrayData.put("content", readmeData);

            jsonData.put("bintray", bintrayData);

            String json = JmeResourceWebsite.getObjectMapper().writeValueAsString(jsonData);

            StringEntity requestEntity = new StringEntity(json, ContentType.APPLICATION_JSON);

            HttpUriRequest request = RequestBuilder.post()
                    .setUri("https://api.bintray.com/packages/" + config.getSubject() + "/" + config.getRepo() + "/" + asset.getPackageName() + "/readme")
                    .setEntity(requestEntity)
                    .build();

            try (CloseableHttpResponse httpResponse = httpClient.execute(request)) {

                if (httpResponse.getStatusLine().getStatusCode() >= 400) {
                    return false;
                }

                return true;
            }

        }

    }
}
