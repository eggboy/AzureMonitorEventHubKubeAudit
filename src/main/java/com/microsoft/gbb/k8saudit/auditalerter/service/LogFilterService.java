package com.microsoft.gbb.k8saudit.auditalerter.service;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LogFilterService {
    public static final String apiFilter = "networkpolicies";

    public void logNetworkPolicy(String message) {

        // Skip the records which doesn't have 'networkpolicies' Event
        if(message.indexOf(apiFilter) != -1) {
            DocumentContext jsonContext = JsonPath.parse(message);

            List<String> jsonpathCreatorLocation = jsonContext.read("$['records'][*]['properties']['log']");

            for(String networkPolicyLog :jsonpathCreatorLocation ) {
                if(networkPolicyLog.contains(apiFilter)) {
                    DocumentContext eventLogContext = JsonPath.parse(networkPolicyLog);
                    log.info("##############################");
                    log.info("Event Timestamp : {}", (String) eventLogContext.read("$['requestReceivedTimestamp']"));
                    log.info("Source IP : {}", (String) eventLogContext.read("$['sourceIPs'][0]"));
                    log.info("User  : {}", (String) eventLogContext.read("$['user']['username']"));
                    log.info("Logs : ", networkPolicyLog);
                    log.info("##############################");
                }
            }
        }
    }
}
