package com.kaayotee.fbaspotter.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kaayotee.fbaspotter.service.IpnConfig;
import com.kaayotee.fbaspotter.exception.IpnException;
import com.kaayotee.fbaspotter.service.IpnHandler;
import com.kaayotee.fbaspotter.domain.IpnInfo;

@RestController
public class PayPalController {

    @Autowired
    IpnHandler ipnHandler;

    @RequestMapping(value="/paypalcallback", method= RequestMethod.POST)
    public IpnInfo handleIPN(HttpServletRequest request) throws IpnException {
        System.out.println("here " + request.getRequestURI());
        IpnConfig ipnConfig = new IpnConfig("https://www.sandbox.paypal.com/cgi-bin/webscr","","","");
        ipnHandler.setIpnConfig(ipnConfig);

        return ipnHandler.handleIpn(request);
    }

}
