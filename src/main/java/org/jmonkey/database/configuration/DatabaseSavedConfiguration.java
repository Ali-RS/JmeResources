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
    private GeneralConfiguration generalConfig;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    public long getId() { return this.id; }
    protected void setId(long id) { this.id = id; }

    @Embedded
    public BintrayConfiguration getBintrayConfig() { 
        if (this.bintrayConfig == null) {
            this.bintrayConfig = new BintrayConfiguration();
        }
        
        return this.bintrayConfig; 
    }
    protected void setBintrayConfig(BintrayConfiguration bintrayConfig) { this.bintrayConfig = bintrayConfig; }

    @Embedded
    public GeneralConfiguration getGeneralConfig() { 
        if (this.generalConfig == null) {
            this.generalConfig = new GeneralConfiguration();
        }
        
        return generalConfig; 
    }
    public void setGeneralConfig(GeneralConfiguration generalConfig) { this.generalConfig = generalConfig; }

}
