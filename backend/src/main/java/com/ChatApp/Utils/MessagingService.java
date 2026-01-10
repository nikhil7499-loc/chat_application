package com.ChatApp.Utils;

import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {

    /**
     * Run:
     */

    @Value("${mailjet.api.public}")
    private String publicKey;

    @Value("${mailjet.api.private}")
    private String privateKey;

    @Value("${mailjet.sender}")
    private String senderEmail;

    public boolean sendEmail(String toEmail, String subject, String body) {
        try {
            MailjetClient client;
            MailjetRequest request;
            MailjetResponse response;
            client = new MailjetClient(publicKey, privateKey);
            request = new MailjetRequest(Emailv31.resource)
                    .property(Emailv31.MESSAGES, new JSONArray()
                            .put(new JSONObject()
                                    .put(Emailv31.Message.FROM, new JSONObject()
                                            .put("Email", senderEmail)
                                            .put("Name", "ChatApp"))
                                    .put(Emailv31.Message.TO, new JSONArray()
                                            .put(new JSONObject()
                                                    .put("Email", toEmail)
                                                    .put("Name", "You")))
                                    .put(Emailv31.Message.SUBJECT, subject)
                                    .put(Emailv31.Message.TEXTPART, body)
                            ));
            response = client.post(request);
            return response.getStatus()==200;

        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
