package com.kaayotee.fbaspotter.service;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.kaayotee.fbaspotter.json.MailChimpList;
import com.kaayotee.fbaspotter.json.MailChimpLists;
import com.kaayotee.fbaspotter.json.Member;
import com.kaayotee.fbaspotter.json.MergeFields;

@Service
public class MailChimpService {

    private static RestTemplate restTemplate = new RestTemplate();;
    private static String mailChimpApiUrl;

    String plainCreds = "maaz:5a3910563094f2932dd151b1effaff38-us13";
    byte[] plainCredsBytes = plainCreds.getBytes();
    byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
    String base64Creds = new String(base64CredsBytes);

    HttpHeaders headers = new HttpHeaders();
    private static HttpEntity<String> request;

    @Autowired
    public MailChimpService() {
        mailChimpApiUrl = "http://us13.api.mailchimp.com/3.0";
        headers.add("Authorization", "Basic " + base64Creds);
        request = new HttpEntity<String>(headers);
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
            chimpList.getName().equalsIgnoreCase(listName);
            return chimpList;
        }
        return null;
    }

/*    public Member addMemberToList(String listName, Member member) {
        MailChimpList mailChimpList = getListByName(listName);
        String url = mailChimpApiUrl + "/lists/" + mailChimpList.getId() + "/members";
        //= restTemplate.exchange(url, HttpMethod.POST, request, Member.class);
        ResponseEntity<Member> responseEntity = restTemplate.postForEntity(url, request, Member.class, member);
        return responseEntity.getBody();
    }*/

    public static void main(String args[]) {
        MailChimpService mailChimpService = new MailChimpService();
        System.out.println("====" + mailChimpService.getLists().getLists().get(0).getName());
        System.out.println("====" + mailChimpService.getListById("6bad6a4b81").getDateCreated());
        System.out.println("====" + mailChimpService.getListByName("SubscriberDaily").getDateCreated());
        Member member = new Member();
        member.setEmailAddress("quraishihina6@gmail.com");
        MergeFields mergeFields = new MergeFields();
        mergeFields.setFNAME("Hina");
        mergeFields.setLNAME("Quraishi");
        member.setMergeFields(mergeFields);
        //System.out.println("====" + mailChimpService.addMemberToList("SubscriberDaily", member));
    }
}
