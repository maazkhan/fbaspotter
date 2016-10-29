package com.kaayotee.fbaspotter.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaayotee.fbaspotter.domain.IpnInfo;
import com.kaayotee.fbaspotter.exception.IpnException;
import com.kaayotee.fbaspotter.json.Member;
import com.kaayotee.fbaspotter.json.MergeFieldEntry;

@Service
public class IpnHandlerService
{

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(IpnHandlerService.class);
    private IpnConfig ipnConfig;

    @Autowired
    private MailChimpService mailChimpService;

    public IpnInfo handleIpn (HttpServletRequest request) throws IpnException {
        LOGGER.info("inside ipn");
        IpnInfo ipnInfo = new IpnInfo();
        try
        {
            //1. Read all posted request parameters
            String requestParams = this.getAllRequestParams(request);
            LOGGER.info(requestParams);

            //2. Prepare 'notify-validate' command with exactly the same parameters
            Enumeration en = request.getParameterNames();
            StringBuilder validationMsg = new StringBuilder("cmd=_notify-validate");
            String paramName;
            String paramValue;
            while (en.hasMoreElements()) {
                paramName = (String) en.nextElement();
                paramValue = request.getParameter(paramName);
                validationMsg.append("&").append(paramName).append("=")
                        .append(paramValue);
            }

            //3. Post above command to Paypal IPN URL {@link IpnConfig#ipnUrl}
            String res = checkStatusOfPayPalMsg(validationMsg.toString());


            //5. Capture Paypal IPN information
            ipnInfo.setFirstName(request.getParameter("first_name"));
            ipnInfo.setLastName(request.getParameter("last_name"));
            ipnInfo.setPayerEmail(request.getParameter("payer_email"));
            ipnInfo.setSubscriptionName(request.getParameter("item_name"));
            ipnInfo.setTxnType(request.getParameter("txn_type"));
            ipnInfo.setPaymentStatus(request.getParameter("payment_status"));
            ipnInfo.setPaymentType(request.getParameter("payment_type"));
            ipnInfo.setPaymentDate(request.getParameter("payment_date"));


            //6. Validate captured Paypal IPN Information
            if (res.equals("VERIFIED") || res.equalsIgnoreCase("<!DOCTYPE html>")) {
                LOGGER.info("Verified from Paypal");

                createMergeField(request, ipnInfo);

                String emailMd5Hash = getMD5Hash(ipnInfo.getPayerEmail().toLowerCase());
                LOGGER.info("MD5 of payer email id " + emailMd5Hash);

                Map<String, String> requestMap = convertMap(request.getParameterMap());
                Member member = new Member();
                member.setEmailAddress(ipnInfo.getPayerEmail());
                member.setStatus("subscribed");
                member.setMergeFields(requestMap);
                ObjectMapper mapper = new ObjectMapper();

                String jsonInString = mapper.writeValueAsString(member);
                LOGGER.info("Json payload to mailchimp " + jsonInString);


                LOGGER.info("Entry for list " + ipnInfo.getSubscriptionName());
                if (ipnInfo.getSubscriptionName().equalsIgnoreCase("SingleList")) {
                    LOGGER.info("Adding member to list Single ");
                    mailChimpService.addMemberToList("SingleList", member, emailMd5Hash);
                } else if(ipnInfo.getSubscriptionName().equalsIgnoreCase("SubscriptionWeekly")) {
                    LOGGER.info("Adding member to list Weekly ");
                    mailChimpService.addMemberToList("SubscriptionWeekly", member, emailMd5Hash);
                } else if(ipnInfo.getSubscriptionName().equalsIgnoreCase("SubscriptionMonthly")) {
                    LOGGER.info("Adding member to list Monthly ");
                    mailChimpService.addMemberToList("SubscriptionMonthly", member, emailMd5Hash);
                } else {
                    LOGGER.info("Adding member to list PayPal");
                    mailChimpService.addMemberToList("PayPal", member, emailMd5Hash);
                }
            }
        }
        catch(Exception e)
        {
            throw new IpnException(e.toString());
        }

        //8. If all is well, return {@link IpnInfo} to the caller for further business logic execution
        return ipnInfo;
    }

    private void createMergeField(HttpServletRequest request, IpnInfo ipnInfo) {
        Set<String> params = request.getParameterMap().keySet();
        for (String param : params) {
            MergeFieldEntry mergeFieldEntry = new MergeFieldEntry();
            mergeFieldEntry.setTag(param.replace("_",""));
            mergeFieldEntry.setName(param);
            LOGGER.info(mergeFieldEntry.getName() + mergeFieldEntry.getTag() + mergeFieldEntry.getType());

            if (mailChimpService.mergeListEntry.contains(param)) {
                if (param.contains("date")) {
                    mergeFieldEntry.setType("date");
                }
                mailChimpService.addMergeFields(mailChimpService.getListByName(ipnInfo.getSubscriptionName()).getId(), mergeFieldEntry);
            }
        }
    }

    private Map<String, String> convertMap(Map<String, String[]> map) {


        Map<String, String> valueMap = new HashMap<>();
        for (Iterator it = map.keySet().iterator(); it.hasNext();) {
            String key = (String) it.next();
            //sb.append(pn).append("\n");
            String[] values = map.get(key);

            if (mailChimpService.mergeListEntry.contains(key)) {
                key = key.replace("_", "");
                if (key.equalsIgnoreCase("firstname")) {
                    key = "FNAME";
                }
                if (key.equalsIgnoreCase("lastname")) {
                    key = "LNAME";
                }
                key = key.toUpperCase();

                if (key.length() > 10) {
                    key = key.substring(0, 10);
                }
                valueMap.put(key, values[0]);
            }

        }
        return valueMap;
    }
    private String getMD5Hash(String s) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(s.getBytes());

        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    private String checkStatusOfPayPalMsg(String validationMsg) {
        String res = null;
        try {
            URL u = new URL(this.getIpnConfig().getIpnUrl());
            HttpsURLConnection uc = (HttpsURLConnection) u.openConnection();
            uc.setDoOutput(true);
            uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            uc.setRequestProperty("Host", "www.paypal.com");
            PrintWriter pw = new PrintWriter(uc.getOutputStream());
            pw.println(validationMsg);
            pw.close();

            //4. Read response from Paypal
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            res = in.readLine();
            in.close();
            LOGGER.info("Response back from paypal " + res);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return res;
    }


    /**
     * Utility method to extract all request parameters and their values from request object
     *
     * @param request {@link HttpServletRequest}
     * @return all request parameters in the form:
     *                                              param-name 1
     *                                                  param-value
     *                                              param-name 2
     *                                                  param-value
     *                                                  param-value (in case of multiple values)
     */
    private String getAllRequestParams(HttpServletRequest request)
    {
        Map map = request.getParameterMap();
        StringBuilder sb = new StringBuilder("\nREQUEST PARAMETERS\n");
        for (Iterator it = map.keySet().iterator(); it.hasNext();)
        {
            String pn = (String)it.next();
            sb.append(pn).append("\n");
            String[] pvs = (String[]) map.get(pn);
            for (int i = 0; i < pvs.length; i++) {
                String pv = pvs[i];
                sb.append("\t").append(pv).append("\n");
            }
        }
        return sb.toString();
    }

    public IpnConfig getIpnConfig() {
        return ipnConfig;
    }

    public void setIpnConfig(IpnConfig ipnConfig) {
        this.ipnConfig = ipnConfig;
    }

}
