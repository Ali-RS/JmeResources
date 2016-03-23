package org.jmonkey.database.asset;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author jayfella
 */

@Entity
@Table(name = "assets")
public class Asset implements Serializable {

    private long id;
    private long discourseAuthorId;
    private String packageName;
    private String licenseType;
    private String readme;

    protected Asset() { }

    public Asset(PotentialAsset pa) {

        this.discourseAuthorId = pa.getDiscourseAuthorId();
        this.packageName = pa.getPackageName();
        this.licenseType = pa.getLicenseType();
        this.readme = pa.getPackageReadme();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    public long getId() { return this.id; }
    protected void setId(long id) { this.id = id; }

    @Column(name = "discourse_id")
    @JsonIgnore
    public long getDiscourseAuthorId() { return discourseAuthorId; }
    protected void setDiscourseAuthorId(long discourseAuthorId) { this.discourseAuthorId = discourseAuthorId; }

    @Column(name = "package_name")
    public String getPackageName() { return packageName; }
    protected void setPackageName(String packageName) { this.packageName = packageName; }

    @Column(name = "license_type")
    public String getLicenseType() { return licenseType; }
    protected void setLicenseType(String licenseType) { this.licenseType = licenseType; }

    @Column(name = "readme")
    public String getReadme() { return readme; }
    public void setReadme(String readme) { this.readme = readme; }

}
