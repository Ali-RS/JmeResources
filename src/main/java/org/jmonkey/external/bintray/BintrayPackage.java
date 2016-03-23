package org.jmonkey.external.bintray;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author jayfella
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class BintrayPackage {

    private String name;


}
