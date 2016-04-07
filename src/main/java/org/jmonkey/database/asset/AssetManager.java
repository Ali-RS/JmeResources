package org.jmonkey.database.asset;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.database.user.User;
import org.jmonkey.database.user.UserManager;

import java.io.IOException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author jayfella
 */

public class AssetManager {

    public List<PotentialAsset> getPotentialAssets() {

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            @SuppressWarnings("unchecked")
            List<PotentialAsset> potentialAssets = session.createCriteria(PotentialAsset.class)
                    .list();

            return potentialAssets;
        }
    }

    public List<PotentialAssetInfo> getPotentialAssetInfos() {

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            List<PotentialAssetInfo> infos = new ArrayList<>();

            @SuppressWarnings("unchecked")
            List<PotentialAsset> potentialAssets = session.createCriteria(PotentialAsset.class)
                    .add(Restrictions.eq("rejected", false))
                    .list();

            potentialAssets.forEach( pa ->  {

                User user = (User) session.createCriteria(User.class)
                        .add(Restrictions.eq("discourseId", pa.getDiscourseAuthorId()))
                        .uniqueResult();

                List<String> dirStructure = new ArrayList<>();

                try {
                    JarFile jarFile = new JarFile(pa.getAssetFile());

                    Enumeration<JarEntry> jarEntries = jarFile.entries();

                    while (jarEntries.hasMoreElements()) {
                        JarEntry entry = jarEntries.nextElement();
                        dirStructure.add(entry.getName());
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                PotentialAssetInfo paInfo = new PotentialAssetInfo(pa, user, dirStructure);
                infos.add(paInfo);
            });

            return infos;
        }
    }

    public List<PotentialAsset> getPotentialAssets(long discourseId) {

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            @SuppressWarnings("unchecked")
            List<PotentialAsset> potentialAssets = session.createCriteria(PotentialAsset.class)
                    .add(Restrictions.eq("discourseAuthorId", discourseId))
                    .list();

            return potentialAssets;
        }
    }

    public List<PotentialAsset> getPotentialAssets(User user) {
        return this.getPotentialAssets(user.getDiscourseId());
    }

    public PotentialAsset getPotentialAsset(long id) {

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            PotentialAsset pa = (PotentialAsset) session.createCriteria(PotentialAsset.class)
                    .add(Restrictions.eq("id", id))
                    .uniqueResult();

            return pa;
        }

    }

    public PotentialAsset getPotentialAsset(User user, String packageName) {

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            PotentialAsset potentialAsset = (PotentialAsset) session.createCriteria(PotentialAsset.class)
                    .add(Restrictions.eq("discourseAuthorId", user.getDiscourseId()))
                    .add(Restrictions.eq("packageName", packageName))
                    .uniqueResult();

            return potentialAsset;
        }

    }

    public List<AssetInfo> getRandomAssets(int amount) {

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            @SuppressWarnings("unchecked")
            List<Asset> assets = session.createCriteria(Asset.class)
                    .add(Restrictions.sqlRestriction("1=1 order by rand()"))
                    .setMaxResults(amount)
                    .list();

            List<AssetInfo> assetInfos = new ArrayList<>();

            UserManager userManager = new UserManager();

            assets.forEach(asset -> {

                User user = userManager.getUser(asset.getDiscourseAuthorId());
                List<AssetVersion> versions = this.getAssetVersions(asset);

                AssetInfo assetInfo = new AssetInfo(user, asset, versions);
                assetInfos.add(assetInfo);
            });

            return assetInfos;
        }
    }

    public AssetInfo getAssetInfo(String username, String packageName) {

        try ( Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {


            User user = (User) session.createCriteria(User.class)
                    .add(Restrictions.eq("username", username))
                    .uniqueResult();

            Asset asset = (Asset) session.createCriteria(Asset.class)
                    .add(Restrictions.eq("discourseAuthorId", user.getDiscourseId()))
                    .add(Restrictions.eq("packageName", packageName))
                    .uniqueResult();

            @SuppressWarnings("unchecked")
            List<AssetVersion> versions = session.createCriteria(AssetVersion.class)
                    .add(Restrictions.eq("assetId", asset.getId()))
                    .list();

            return new AssetInfo(user, asset, versions);
        }

    }

    public AssetInfo getAssetInfo(String username, String packageName, String version) {

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            User user = (User) session.createCriteria(User.class)
                    .add(Restrictions.eq("username", username))
                    .uniqueResult();

            Asset asset = (Asset) session.createCriteria(Asset.class)
                    .add(Restrictions.eq("discourseAuthorId", user.getDiscourseId()))
                    .add(Restrictions.eq("packageName", packageName))
                    .uniqueResult();

            if (asset == null) {
                return null;
            }

            AssetVersion assetVersion = (AssetVersion) session.createCriteria(AssetVersion.class)
                    .add(Restrictions.eq("assetId", asset.getId()))
                    .add((Restrictions.eq("version", version)))
                    .uniqueResult();

            return new AssetInfo(user, asset, assetVersion);
        }

    }

    public List<AssetVersion> getAssetVersions(Asset asset) {

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            @SuppressWarnings("unchecked")
            List<AssetVersion> assetVersions = session.createCriteria(AssetVersion.class)
                    .add(Restrictions.eq("assetId", asset.getId()))
                    .list();

            return assetVersions;
        }

    }

    public List<AssetInfo> getAssets(User user) {

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            List<AssetInfo> assetInfos = new ArrayList<>();

            @SuppressWarnings("unchecked")
            List<Asset> assets = session.createCriteria(Asset.class)
                    .add(Restrictions.eq("discourseAuthorId", user.getDiscourseId()))
                    .list();

            assets.forEach(asset -> {

                @SuppressWarnings("unchecked")
                List<AssetVersion> versions = session.createCriteria(AssetVersion.class)
                        .add(Restrictions.eq("assetId", asset.getId()))
                        .list();

                assetInfos.add(new AssetInfo(user, asset, versions));
            });

            return assetInfos;
        }

    }
}
