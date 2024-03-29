package com.kaayotee.fbaspotter.service;

import java.util.Arrays;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.kaayotee.fbaspotter.json.MailChimpList;
import com.kaayotee.fbaspotter.json.MailChimpLists;
import com.kaayotee.fbaspotter.json.Member;
import com.kaayotee.fbaspotter.json.MergeFieldEntry;

@Service
public class MailChimpService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MailChimpService.class);
    private RestTemplate restTemplate = new RestTemplate();;
    private String mailChimpApiUrl;

    List<String> mergeListEntry;
    String plainCreds = "maaz:5a3910563094f2932dd151b1effaff38-us13";
    byte[] plainCredsBytes = plainCreds.getBytes();
    byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
    String base64Creds = new String(base64CredsBytes);

    HttpHeaders headers = new HttpHeaders();
    private HttpEntity<String> request;

    public MailChimpService() {
        this.mailChimpApiUrl = "http://us13.api.mailchimp.com/3.0";
        this.headers.add("Authorization", "Basic " + base64Creds);
        this.request = new HttpEntity<>(headers);
        this.mergeListEntry = Arrays.asList("payment_type","first_name", "last_name", "payment_date", "payer_email","subscr_date","txn_type", "item_name","payment_status","subscr_date");
    }

    public MailChimpLists getLists() {
        ResponseEntity<MailChimpLists> responseEntity = restTemplate.exchange(mailChimpApiUrl + "/lists" , HttpMethod.GET, request, MailChimpLists.class);
        return responseEntity.getBody();
    }

    public MailChimpList getListById(String listId) {
        ResponseEntity<MailChimpList> responseEntity = restTemplate.exchange(mailChimpApiUrl + "/lists/" + listId, HttpMethod.GET, request, MailChimpList.class);
        return responseEntity.getBody();
    }

    public MailChimpList getListByName(String listName) {
        MailChimpLists mailChimpLists = getLists();
        for (MailChimpList chimpList : mailChimpLists.getLists()) {
            if (chimpList.getName().equalsIgnoreCase(listName)) {
                return chimpList;
            }
        }
        return null;
    }

    public ResponseEntity addMergeFields(String listId, MergeFieldEntry mergeFieldEntry) {
        String url = mailChimpApiUrl + "/lists/" + listId + "/merge-fields";

        HttpEntity<MergeFieldEntry> httpEntity = new HttpEntity<MergeFieldEntry> (mergeFieldEntry, headers);
        ResponseEntity<MergeFieldEntry> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, MergeFieldEntry.class);
            LOGGER.info("Response from MailChimp: " + responseEntity.getStatusCode() + " " + responseEntity.getBody());
        } catch (Exception e) {
            LOGGER.info("Filed already added to list ");
        }
        return responseEntity;
    }

   public ResponseEntity addMemberToList(String listName, Member member) {
        MailChimpList mailChimpList = getListByName(listName);
        String url = mailChimpApiUrl + "/lists/" + mailChimpList.getId() + "/members";
        HttpEntity<Member> httpEntity = new HttpEntity<Member> (member, headers);
        ResponseEntity<Member> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Member.class);
        LOGGER.info("Response from MailChimp: " + responseEntity.getStatusCode() + " " + responseEntity.getBody());
        return responseEntity;
    }

    public ResponseEntity addMemberToList(String listName, Member member, String md5) {
        MailChimpList mailChimpList = getListByName(listName);
        String url = mailChimpApiUrl + "/lists/" + mailChimpList.getId() + "/members/" + md5;
        HttpEntity<Member> httpEntity = new HttpEntity<Member> (member, headers);
        ResponseEntity responseEntity = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, Member.class);
        LOGGER.info("Response from MailChimp: " + responseEntity.getStatusCode() + " " + responseEntity.getBody());
        return responseEntity;
    }


/*    public static void main(String args[]) {
        MailChimpService mailChimpService = new MailChimpService();
        System.out.println("====" + mailChimpService.getLists().getLists().get(0).getName());
        System.out.println("====" + mailChimpService.getListById("6bad6a4b81").getDateCreated());
        System.out.println("====" + mailChimpService.getListByName("PayPal").getDateCreated());
        *//*Member member = new Member();
        member.setEmailAddress("quraishihina6@gmail.com");
        member.setStatus("subscribed");
        MergeFields mergeFields = new MergeFields();
        mergeFields.setFNAME("Hina");
        mergeFields.setLNAME("Quraishi");
        member.setMergeFields(mergeFields);
        System.out.println("====" + mailChimpService.addMemberToList("SubscriberDaily", member));*//*

        MailChimpList mailChimpList = mailChimpService.getListByName("PayPal");
        MergeFieldEntry mergeFieldEntry = new MergeFieldEntry();
        mergeFieldEntry.setTag("AWESOME");
        mergeFieldEntry.setName("Awesome Address");
        System.out.println("---> "+ mailChimpService.addMergeFields(mailChimpList.getId(), mergeFieldEntry));
    }*/



}
