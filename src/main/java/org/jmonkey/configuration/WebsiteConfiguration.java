package org.jmonkey.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.nio.file.Path;

/**
 * @author jayfella
 */

public class WebsiteConfiguration {
    
    private Path htmlPath;
    private Path themesPath;
    private Path activeThemePath;
    private Path permissionsPath;
    private Path uploadsPath;
    
    @JsonProperty("html-path")
    @JsonDeserialize(using = PathDeserializer.class)
    public Path getHtmlPath() { return this.htmlPath; }
    protected void setHtmlPath(Path htmlPath) { this.htmlPath = htmlPath; }
    
    @JsonProperty("themes-path")
    @JsonDeserialize(using = PathDeserializer.class)
    public Path getThemesPath() { return this.themesPath; }
    protected void setThemesPath(Path themesPath) { this.themesPath = themesPath; }
    
    @JsonProperty("active-theme-path")
    @JsonDeserialize(using = PathDeserializer.class)
    public Path getActiveThemePath() { return this.activeThemePath; }
    protected void setActiveThemePath(Path activeThemePath) { this.activeThemePath = activeThemePath; }

    @JsonProperty("permissions-path")
    @JsonDeserialize(using = PathDeserializer.class)
    public Path getPermissionsPath() { return this.permissionsPath; }
    protected void setPermissionsPath(Path permissionsPath) { this.permissionsPath = permissionsPath; }

    @JsonProperty("uploads-path")
    @JsonDeserialize(using = PathDeserializer.class)
    public Path getUploadsPath() { return uploadsPath; }
    protected void setUploadsPath(Path uploadsPath) { this.uploadsPath = uploadsPath; }
}
