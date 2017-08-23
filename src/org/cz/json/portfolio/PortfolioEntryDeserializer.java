package org.cz.json.portfolio;

import java.io.IOException;

import org.cz.json.portfolio.hs.PortfolioEntryHs;
import org.cz.json.portfolio.mp.PortfolioEntryMp;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdNodeBasedDeserializer;

public class PortfolioEntryDeserializer extends StdNodeBasedDeserializer<PortfolioEntry> {

	private static final long serialVersionUID = 1L;

	protected PortfolioEntryDeserializer() {
		super(PortfolioEntry.class);
	}
	
	@Override
	public PortfolioEntry convert(JsonNode root, DeserializationContext ctxt) throws IOException {
		java.lang.reflect.Type targetType;
	    if (root.has("portfolioEntryHsUnique")) {
	        targetType = PortfolioEntryHs.class;
	    } else 
	    if (root.has("portfolioEntryMpUnique")) {
	  	        targetType = PortfolioEntryMp.class;
	  	    } 
	    else {
	        targetType = PortfolioEntry.class;
	    }
	    
		JavaType jacksonType = ctxt.getTypeFactory().constructType(targetType);
	    JsonDeserializer<?> deserializer = ctxt.findRootValueDeserializer(jacksonType);
	    JsonParser nodeParser = root.traverse(ctxt.getParser().getCodec());
	    nodeParser.nextToken();
	    return (PortfolioEntry) deserializer.deserialize(nodeParser, ctxt);
	}
	
}
