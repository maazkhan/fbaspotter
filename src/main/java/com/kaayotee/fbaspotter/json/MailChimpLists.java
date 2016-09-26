
package com.kaayotee.fbaspotter.json;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * Subscriber Lists
 * <p>
 * A collection of subscriber lists associated with this account. Lists contain subscribers who have opted-in to receive correspondence from you or your organization.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "lists",
        "total_items"
})
public class MailChimpLists {

    /**
     * Lists
     * <p>
     * An array of objects, each representing a list resource.
     *
     */
    @JsonProperty("lists")
    private List<MailChimpList> lists = new ArrayList<MailChimpList>();
    /**
     * Item Count
     * <p>
     * The total number of items matching the query, irrespective of pagination.
     *
     */
    @JsonProperty("total_items")
    private Integer totalItems;

    /**
     * Lists
     * <p>
     * An array of objects, each representing a list resource.
     *
     * @return
     *     The lists
     */
    @JsonProperty("lists")
    public List<MailChimpList> getLists() {
        return lists;
    }

    /**
     * Lists
     * <p>
     * An array of objects, each representing a list resource.
     *
     * @param lists
     *     The lists
     */
    @JsonProperty("lists")
    public void setLists(List<MailChimpList> lists) {
        this.lists = lists;
    }

    /**
     * Item Count
     * <p>
     * The total number of items matching the query, irrespective of pagination.
     *
     * @return
     *     The totalItems
     */
    @JsonProperty("total_items")
    public Integer getTotalItems() {
        return totalItems;
    }

    /**
     * Item Count
     * <p>
     * The total number of items matching the query, irrespective of pagination.
     *
     * @param totalItems
     *     The total_items
     */
    @JsonProperty("total_items")
    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

}
