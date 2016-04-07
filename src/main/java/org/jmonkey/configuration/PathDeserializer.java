package org.jmonkey.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Tatu Saloranta
 */

public class PathDeserializer extends StdScalarDeserializer<Path>
{
    public PathDeserializer() { 
        super(Path.class);
    }
    
    @Override
    public Path deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return Paths.get(jp.getValueAsString());
    }

}