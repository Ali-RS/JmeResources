package org.jmonkey.database.asset;

import org.jmonkey.database.user.User;

import java.util.List;

/**
 * @author jayfella
 */

public class PotentialAssetInfo {

    private PotentialAsset potentialAsset;
    private User user;
    private List<String> jarEntries;

    public PotentialAssetInfo(PotentialAsset potentialAsset, User user, List<String> jarEntries) {
        this.potentialAsset = potentialAsset;
        this.user = user;
        this.jarEntries = jarEntries;
    }

    public PotentialAsset getPotentialAsset() { return potentialAsset; }
    public void setPotentialAsset(PotentialAsset potentialAsset) { this.potentialAsset = potentialAsset; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<String> getJarEntries() { return jarEntries; }
    public void setJarEntries(List<String> jarEntries) { this.jarEntries = jarEntries; }

}
