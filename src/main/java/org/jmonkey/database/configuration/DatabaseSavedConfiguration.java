package org.jmonkey.database.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author jayfella
 */

@Entity
@Table(name = "config")
public class DatabaseSavedConfiguration implements Serializable {

    private long id;

    private BintrayConfiguration bintrayConfig;
    private DiscourseConfiguration discourseConfig;
    private GeneralConfiguration generalConfig;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    public long getId() { return this.id; }
    protected void setId(long id) { this.id = id; }

    @Embedded
    public BintrayConfiguration getBintrayConfig() { return this.bintrayConfig; }
    protected void setBintrayConfig(BintrayConfiguration bintrayConfig) { this.bintrayConfig = bintrayConfig; }

    @Embedded
    public DiscourseConfiguration getDiscourseConfig() { return discourseConfig; }
    protected void setDiscourseConfig(DiscourseConfiguration discourseConfig) { this.discourseConfig = discourseConfig; }

    @Embedded
    public GeneralConfiguration getGeneralConfig() { return generalConfig; }
    public void setGeneralConfig(GeneralConfiguration generalConfig) { this.generalConfig = generalConfig; }

}
