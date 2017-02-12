package rest.restclient;

/**
 * Convert from POJO to JSON and JSON to POJO   
 * @author Shailendra Soni
 */
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import rest.model.JSONPojo;

public class JSONConvertor<T extends JSONPojo> {

    private static final String EMPTY_STRING="";
    
    public T jsonToPojo(String jsonString, Class<T> classzz) throws JsonGenerationException, JsonMappingException, IOException {

        ObjectMapper objMapper = new ObjectMapper();
        JsonNode jsonNode = objMapper.readTree(jsonString);
        objMapper.configure(MapperFeature.AUTO_DETECT_FIELDS, true);
        objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objMapper.readValue(jsonNode.traverse(), classzz);
    }

    public String pojoToJson(T t) throws JsonProcessingException {
        if (t == null) {
            return EMPTY_STRING;
        }
        ObjectMapper objMapper = new ObjectMapper();
        return objMapper.writeValueAsString(t);
    }

}
