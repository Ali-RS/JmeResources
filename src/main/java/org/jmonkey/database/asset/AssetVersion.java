package org.jmonkey.database.asset;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * @author jayfella
 */

@Entity
@Table(name = "asset_versions")
public class AssetVersion {

    private long id;

    private long assetId;

    private String version;
    private String showcaseImage;
    private String description;
    private long timeUploaded;

    protected AssetVersion() { }

    public AssetVersion(String version) {
        this.version = version;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    public long getId() { return this.id; }
    protected void setId(long id) { this.id = id; }

    @Column(name = "asset_id")
    @JsonIgnore
    public long getAssetId() { return assetId; }
    public void setAssetId(long assetId) { this.assetId = assetId; }

    @Column(name = "version")
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    @Column(name = "showcase_image")
    public String getShowcaseImage() { return showcaseImage; }
    public void setShowcaseImage(String showcaseImage) { this.showcaseImage = showcaseImage; }

    @Column(name = "description")
    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }

    @Column(name = "time_uploaded")
    public long getTimeUploaded() { return timeUploaded; }
    public void setTimeUploaded(long timeUploaded) { this.timeUploaded = timeUploaded; }

}
