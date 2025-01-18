package pt.ipleiria.estg.dei.ei.dae.backendappmonitor.DTOs;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericDTO<P, T> {
    private String parentName; // Dynamic name for the first property
    private P parentValue;     // Value for the first property (String or Long)
    private String listName;   // Dynamic name for the second property
    private T listValue;       // Value for the second property

    public GenericDTO(String parentName, P parentValue, String listName, T listValue) {
        this.parentName = parentName;
        this.parentValue = parentValue;
        this.listName = listName;
        this.listValue = listValue;
    }

    @JsonAnyGetter
    public Map<String, Object> getDynamicProperties() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(parentName, parentValue);
        map.put(listName, listValue);
        return map;
    }
}
