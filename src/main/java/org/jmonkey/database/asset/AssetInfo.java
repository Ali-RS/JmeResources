package org.jmonkey.database.asset;

import org.codehaus.jackson.annotate.JsonProperty;
import org.jmonkey.database.user.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author jayfella
 */

public class AssetInfo {

    private Asset asset;
    private User user;
    private List<AssetVersion> versions;

    public AssetInfo(User user, Asset asset, List<AssetVersion> versions) {
        this.asset = asset;
        this.user = user;
        this.versions = versions;
    }

    public AssetInfo(User user, Asset asset, AssetVersion version) {
        this.asset = asset;
        this.user = user;
        this.versions = Collections.singletonList(version);
    }

    public Asset getAsset() { return asset; }
    public User getUser() { return user; }
    public List<AssetVersion> getVersions() { return versions; }
}
