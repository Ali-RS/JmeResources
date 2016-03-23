package org.jmonkey.database.asset;

import javax.persistence.*;
import java.io.Serializable;
import java.nio.file.Path;

/**
 * @author jayfella
 */

@Entity
@Table(name = "potential_assets")
public class PotentialAsset implements Serializable {

    private long id;

    private long discourseAuthorId;
    private String packageName;
    private String licenseType;
    private String showcaseImage;
    private String versionDescription;
    private String packageReadme;
    private String assetFile;
    private long dateUploaded;

    public PotentialAsset() {}

    public PotentialAsset(long discourseAuthorId, String packageName, String licenseType, String showcaseImage, String versionDescription, String packageReadme, String assetFile) {

        this.discourseAuthorId = discourseAuthorId;
        this.packageName = packageName;
        this.licenseType = licenseType;
        this.showcaseImage = showcaseImage;
        this.versionDescription = versionDescription;
        this.packageReadme = packageReadme;
        this.assetFile = assetFile;
        this.dateUploaded = System.currentTimeMillis();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() { return this.id; }
    protected void setId(long id) { this.id = id; }

    @Column(name = "author")
    public long getDiscourseAuthorId() { return discourseAuthorId; }
    protected void setDiscourseAuthorId(long discourseAuthorId) { this.discourseAuthorId = discourseAuthorId; }

    @Column(name = "package_name")
    public String getPackageName() { return packageName; }
    protected void setPackageName(String packageName) { this.packageName = packageName; }

    @Column(name = "license_type")
    public String getLicenseType() { return licenseType; }
    protected void setLicenseType(String licenseType) { this.licenseType = licenseType; }

    @Column(name = "showcase_image")
    public String getShowcaseImage() { return showcaseImage; }
    protected void setShowcaseImage(String showcaseImage) { this.showcaseImage = showcaseImage; }

    @Column(name = "version_description")
    public String getVersionDescription() { return this.versionDescription; }
    protected void setVersionDescription(String versionDescription) { this.versionDescription = versionDescription; }

    @Column(name = "package_readme", length = 16384)
    public String getPackageReadme() { return packageReadme; }
    protected void setPackageReadme(String packageReadme) { this.packageReadme = packageReadme; }

    @Column(name = "asset_file")
    public String getAssetFile() { return assetFile; }
    protected void setAssetFile(String assetFile) { this.assetFile = assetFile; }

    @Column(name = "date_uploaded")
    public long getDateUploaded() { return this.dateUploaded; }
    protected void setDateUploaded(long dateUploaded) { this.dateUploaded = dateUploaded; }

}
