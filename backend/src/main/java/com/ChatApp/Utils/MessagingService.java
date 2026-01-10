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

    @Value("${mailjet.api.public}")
    private String publicKey;

    @Value("${mailjet.api.private}")
    private String privateKey;

    @Value("${mailjet.sender}")
    private String senderEmail;

    public boolean sendEmail(String toEmail, String subject, String body) {

        try {
            MailjetClient client = new MailjetClient(publicKey, privateKey);

            MailjetRequest request = new MailjetRequest(Emailv31.resource)
                    .property(Emailv31.MESSAGES, new JSONArray()
                            .put(new JSONObject()
                                    .put(Emailv31.Message.FROM, new JSONObject()
                                            .put("Email", senderEmail)
                                            .put("Name", "ChatApp"))
                                    .put(Emailv31.Message.TO, new JSONArray()
                                            .put(new JSONObject()
                                                    .put("Email", toEmail)))
                                    .put(Emailv31.Message.SUBJECT, subject)
                                    .put(Emailv31.Message.TEXTPART, body)
                                    .put(Emailv31.Message.HTMLPART,
                                            "<h3>" + body + "</h3>")
                            ));

            MailjetResponse response = client.post(request);

            System.out.println("Mailjet Status: " + response.getStatus());
            System.out.println(response.getData());

            return response.getStatus() == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
