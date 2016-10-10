package com.kaayotee.fbaspotter.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaayotee.fbaspotter.domain.IpnInfo;
import com.kaayotee.fbaspotter.exception.IpnException;
import com.kaayotee.fbaspotter.json.Member;
import com.kaayotee.fbaspotter.json.MergeFields;

@Service
public class IpnHandlerService
{

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(IpnHandlerService.class);
    private IpnConfig ipnConfig;

    @Autowired
    private MailChimpService mailChimpService;
    /**
     * This method handles the Paypal IPN Notification as follows:
     *      1. Read all posted request parameters
     *      2. Prepare 'notify-validate' command with exactly the same parameters
     *      3. Post above command to Paypal IPN URL {@link IpnConfig#ipnUrl}
     *      4. Read response from Paypal
     *      5. Capture Paypal IPN information
     *      6. Validate captured Paypal IPN Information
     *          6.1. Check that paymentStatus=Completed
     *          6.2. Check that txnId has not been previously processed
     *          6.3. Check that receiverEmail matches with configured {@link IpnConfig#receiverEmail}
     *          6.4. Check that paymentAmount matches with configured {@link IpnConfig#paymentAmount}
     *          6.5. Check that paymentCurrency matches with configured {@link IpnConfig#paymentCurrency}
     *      7. In case of any failed validation checks, throw {@link IpnException}
     *      8. If all is well, return {@link IpnInfo} to the caller for further business logic execution
     *
     * @param request {@link HttpServletRequest}
     * @return {@link IpnInfo}
     * @throws IpnException
     */
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
                        .append(URLEncoder.encode(paramValue, request.getParameter("charset")));
            }

            //3. Post above command to Paypal IPN URL {@link IpnConfig#ipnUrl}
            String res = checkStatusOfPayPalMsg(validationMsg.toString());


            //5. Capture Paypal IPN information
            ipnInfo.setFirstName(request.getParameter("first_name"));
            ipnInfo.setLastName(request.getParameter("last_name"));
            ipnInfo.setPayerEmail(request.getParameter("payer_email"));
            ipnInfo.setSubscriptionName(request.getParameter("item_name"));
            ipnInfo.setTxnType(request.getParameter("txn_type"));
    

            //6. Validate captured Paypal IPN Information
            if (res.equals("VERIFIED") && ipnInfo.getTxnType() != null) {
                LOGGER.info("Verified from Paypal");
                if (ipnInfo.getTxnType().equalsIgnoreCase("subscr_signup")) {
                    LOGGER.info("Adding New Subscription");
                    Member member = new Member();
                    member.setEmailAddress(ipnInfo.getPayerEmail());
                    member.setStatus("subscribed");
                    MergeFields mergeFields = new MergeFields();
                    mergeFields.setFNAME(ipnInfo.getFirstName());
                    mergeFields.setLNAME(ipnInfo.getLastName());
                    member.setMergeFields(mergeFields);
                    mailChimpService.addMemberToList(ipnInfo.getSubscriptionName(), member);
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
            //sb.append(pn).append("\n");
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