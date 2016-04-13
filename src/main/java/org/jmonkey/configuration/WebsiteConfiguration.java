package org.jmonkey.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.nio.file.Path;

/**
 * @author jayfella
 */

public class WebsiteConfiguration {
    
    private Path uploadsPath;
    
    @JsonProperty("uploads-path")
    @JsonDeserialize(using = PathDeserializer.class)
    public Path getUploadsPath() { return uploadsPath; }
    protected void setUploadsPath(Path uploadsPath) { this.uploadsPath = uploadsPath; }
}
