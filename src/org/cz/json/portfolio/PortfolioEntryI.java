package org.cz.json.portfolio;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = PortfolioEntryDeserializer.class)

public interface PortfolioEntryI {

}
