package org.jmonkey.external.bintray;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 *
 * @author jayfella
 */
public class BintrayFile {
    
    private String name;
    private String repo;
    private String owner;
    private String desc;
    private List<String> labels;
    private List<String> attributeNames;
    private List<String> licenses;
    private List<String> customLicenses;
    private int followersCount;
    private String created;
    private String websiteUrl;
    private String issueTrackerUrl;
    private List<String> linkedToRepos;
    private List<String> permissions;
    private List<String> versions;
    private String latestVersion;
    private String updated;
    private int ratingCount;
    private List<String> systemIds;
    private String vcsUrl;
    private String maturity;
    
    @JsonProperty("name")
    public String getName() { return this.name; }
    protected void setName(String name) { this.name = name; }
    
    @JsonProperty("repo")
    public String getRepository() { return this.repo; }
    protected void setRepository(String repo) { this.repo = repo; }
    
    @JsonProperty("owner")
    public String getOwner() { return this.owner; }
    protected void setOwner(String owner) { this.owner = owner; }
    
    @JsonProperty("desc")
    public String getDescription() { return this.desc; }
    protected void setDescription(String desc) { this.desc = desc; }
    
    @JsonProperty("labels")
    public List<String> getLabels() { return this.labels; }
    protected void setLabels(List<String> labels) { this.labels = labels; }
    
    @JsonProperty("attribute_names")
    public List<String> getAttributeNames() { return this.attributeNames; }
    protected void setAttributeNames(List<String> attributeNames) { this.attributeNames = attributeNames; }
    
    @JsonProperty("licenses")
    public List<String> getLicenses() { return this.licenses; }
    protected void setLicenses(List<String> licenses) { this.licenses = licenses; }
    
    @JsonProperty("custom_licenses")
    public List<String> getCustomLicenses() { return this.customLicenses; }
    protected void setCustomLicenses(List<String> customLicenses) { this.customLicenses = customLicenses; }
    
    @JsonProperty("followers_count")
    public int getFollowersCount() { return this.followersCount; }
    protected void setFollowersCount(int followersCount) { this.followersCount = followersCount; }
    
    @JsonProperty("created")
    public String getCreated() { return this.created; }
    protected void setCreated(String created) { this.created = created; }
    
    @JsonProperty("website_url")
    public String getWebsiteUrl() { return this.websiteUrl; }
    protected void setWebsiteUrl(String websiteUrl) { this.websiteUrl = websiteUrl; }
    
    @JsonProperty("issue_tracker_url")
    public String getIssueTrackerUrl() { return this.issueTrackerUrl; }
    protected void setIssueTrackerUrl(String issueTrackerUrl) { this.issueTrackerUrl = issueTrackerUrl; }
    
    @JsonProperty("linked_to_repos")
    public List<String> getLinkedToRepositories() { return this.linkedToRepos; }
    protected void setLinkedToRepositories(List<String> linkedToRepos) { this.linkedToRepos = linkedToRepos; }
    
    @JsonProperty("permissions")
    public List<String> getPermissions() { return this.permissions; }
    protected void setPermissions(List<String> permissions) { this.permissions = permissions; }
    
    @JsonProperty("versions")
    public List<String> getVersions() { return this.versions; }
    protected void setVersions(List<String> versions) { this.versions = versions; }
    
    @JsonProperty("latest_version")
    public String getLatestVersion() { return this.latestVersion; }
    protected void setLatestVersion(String latestVersion) { this.latestVersion = latestVersion; }
    
    @JsonProperty("updated")
    public String getUpdated() { return this.updated; }
    protected void setUpdated(String updated) { this.updated = updated; }
    
    @JsonProperty("rating_count")
    public int getRatingCount() { return this.ratingCount; }
    protected void setRatingCount(int ratingCount) { this.ratingCount = ratingCount; }
    
    @JsonProperty("system_ids")
    public List<String> getSystemIds() { return this.systemIds; }
    protected void setSystemIds(List<String> systemIds) { this.systemIds = systemIds; }
    
    @JsonProperty("vcs_url")
    public String getVersionControlUrl() { return this.vcsUrl; }
    protected void setVersionControlUrl(String vcsUrl) { this.vcsUrl = vcsUrl; }
    
    @JsonProperty("maturity")
    public String getMaturity() { return this.maturity; }
    protected void setMaturity(String maturity) { this.maturity = maturity; }
}
