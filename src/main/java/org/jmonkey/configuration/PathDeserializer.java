/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jmonkey.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Tatu Saloranta
 * @see: https://github.com/FasterXML/jackson-datatype-jdk7/blob/master/src/main/java/com/fasterxml/jackson/datatype/jdk7/PathDeserializer.java
 */
public class PathDeserializer extends StdScalarDeserializer<Path>
{
    private static final long serialVersionUID = 1;

    public PathDeserializer() { super(Path.class); }
    
    @Override
    public Path deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException
    {
        return Paths.get(jp.getValueAsString());
    }

}