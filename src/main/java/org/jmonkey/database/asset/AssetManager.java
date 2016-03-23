package org.jmonkey.database.asset;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.jmonkey.JmeResourceWebsite;
import org.jmonkey.database.user.User;
import org.jmonkey.database.user.UserManager;

import java.util.ArrayList;
import java.util.List;

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

    public void savePotentialAsset(PotentialAsset potentialAsset) {

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {
            session.beginTransaction();
            session.save(potentialAsset);
            session.getTransaction().commit();
            session.flush();
        }

    }

    public void deletePotentialAsset(PotentialAsset potentialAsset) {

        try(Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            session.beginTransaction();
            session.delete(potentialAsset);
            session.getTransaction().commit();
            session.flush();
        }

    }



    public Asset saveOrUpdateAsset(Asset asset) {

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            session.beginTransaction();
            session.saveOrUpdate(asset);
            session.getTransaction().commit();
            session.flush();

            return asset;
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



    public List<AssetVersion> getAssetVersions(Asset asset) {

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            @SuppressWarnings("unchecked")
            List<AssetVersion> assetVersions = session.createCriteria(AssetVersion.class)
                    .add(Restrictions.eq("assetId", asset.getId()))
                    .list();

            return assetVersions;
        }

    }

    public void saveOrUpdateAssetVersion(AssetVersion assetVersion) {

        try (Session session = JmeResourceWebsite.getInstance().getDatabaseManager().openSession()) {

            session.beginTransaction();
            session.saveOrUpdate(assetVersion);
            session.getTransaction().commit();
            session.flush();
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
