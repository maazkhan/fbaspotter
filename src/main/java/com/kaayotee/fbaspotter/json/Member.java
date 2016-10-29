
package com.kaayotee.fbaspotter.json;

import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "email_address",
    "status",
    "merge_fields"
})
public class Member {

    @JsonProperty("email_address")
    private String emailAddress;
    @JsonProperty("status")
    private String status;
    @JsonProperty("merge_fields")
    private Map<String, String> mergeFields;

    public Map<String, String> getMergeFields() {
        return mergeFields;
    }

    public void setMergeFields(Map<String, String> mergeFields) {
        this.mergeFields = mergeFields;
    }

    /**
     * 
     * @return
     *     The emailAddress
     */
    @JsonProperty("email_address")
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * 
     * @param emailAddress
     *     The email_address
     */
    @JsonProperty("email_address")
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * 
     * @return
     *     The status
     */
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }



}
