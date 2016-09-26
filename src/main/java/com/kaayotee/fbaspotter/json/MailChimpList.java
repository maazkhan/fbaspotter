
package com.kaayotee.fbaspotter.json;

import java.util.Date;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Subscriber List
 * <p>
 * Information about a specific list.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "id",
        "name",
        "notify_on_subscribe",
        "notify_on_unsubscribe",
        "date_created",
        "list_rating"
})
public class MailChimpList {


    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;


    @JsonProperty("date_created")
    private Date dateCreated;

    @JsonProperty("list_rating")
    private Integer listRating;


    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }


    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("date_created")
    public Date getDateCreated() {
        return dateCreated;
    }

    @JsonProperty("date_created")
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @JsonProperty("list_rating")
    public Integer getListRating() {
        return listRating;
    }

    @JsonProperty("list_rating")
    public void setListRating(Integer listRating) {
        this.listRating = listRating;
    }

}
