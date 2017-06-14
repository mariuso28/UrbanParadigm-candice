package org.cz.json.portfolio;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.cz.json.portfolio.hs.PortfolioEntryHs;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PortfolioEntryDeserializer extends JsonDeserializer<PortfolioEntry> {

	@Override
	public PortfolioEntry deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode jsonNode = p.readValueAsTree(); 
	    Iterator<Map.Entry<String, JsonNode>> ite = jsonNode.fields();
	    boolean isSubclass = false;
	    while (ite.hasNext()) {
	        Map.Entry<String, JsonNode> entry = ite.next();
	        // **Check if it contains field name unique to subclass**
	        if (entry.getKey().equalsIgnoreCase("portfolioEntryHsUnique")) {
	            isSubclass = true;
	            break;
	        }
	    }
	    if (isSubclass) {
	        return new ObjectMapper().treeToValue(jsonNode, PortfolioEntryHs.class);
	    } else {
	    	return new ObjectMapper().treeToValue(jsonNode, PortfolioEntry.class);
	    }
	}

}
