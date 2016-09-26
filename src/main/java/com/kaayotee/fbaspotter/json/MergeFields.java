
package com.kaayotee.fbaspotter.json;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "FNAME",
    "LNAME"
})
public class MergeFields {

    @JsonProperty("FNAME")
    private String fNAME;
    @JsonProperty("LNAME")
    private String lNAME;

    /**
     * 
     * @return
     *     The fNAME
     */
    @JsonProperty("FNAME")
    public String getFNAME() {
        return fNAME;
    }

    /**
     * 
     * @param fNAME
     *     The FNAME
     */
    @JsonProperty("FNAME")
    public void setFNAME(String fNAME) {
        this.fNAME = fNAME;
    }

    /**
     * 
     * @return
     *     The lNAME
     */
    @JsonProperty("LNAME")
    public String getLNAME() {
        return lNAME;
    }

    /**
     * 
     * @param lNAME
     *     The LNAME
     */
    @JsonProperty("LNAME")
    public void setLNAME(String lNAME) {
        this.lNAME = lNAME;
    }

}
