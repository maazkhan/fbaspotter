
package com.kaayotee.fbaspotter.json;

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
    private MergeFields mergeFields;

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

    /**
     * 
     * @return
     *     The mergeFields
     */
    @JsonProperty("merge_fields")
    public MergeFields getMergeFields() {
        return mergeFields;
    }

    /**
     * 
     * @param mergeFields
     *     The merge_fields
     */
    @JsonProperty("merge_fields")
    public void setMergeFields(MergeFields mergeFields) {
        this.mergeFields = mergeFields;
    }

}
