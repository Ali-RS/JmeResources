package org.jmonkey.server.controller;

import java.io.IOException;
import java.util.List;

import org.jmonkey.external.bintray.BintrayApiClient;
import org.jmonkey.external.bintray.BintrayFile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author jayfella
 */

@Controller
public class BintrayController {
    
    @RequestMapping(value = "/search/{term}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<BintrayFile> getResources(@PathVariable String term) throws IOException {
        
        BintrayApiClient client = new BintrayApiClient();
        
        List<BintrayFile> results = client.search(term);
        return results;
    }
    
    @RequestMapping(value = "/get/{term}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BintrayFile getResource(@PathVariable String term) throws IOException {
        
        BintrayApiClient client = new BintrayApiClient();
        
        BintrayFile result = client.get(term);
        return result;
    }
    
    @RequestMapping(value = "/readme/{term}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getReadme(@PathVariable String term) throws IOException {
        
        BintrayApiClient client = new BintrayApiClient();
        
        String result = client.getPackageReadme(term);
        return result;
    }
}
