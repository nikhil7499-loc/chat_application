package com.ChatApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

import java.time.ZoneId;
import java.util.TimeZone;

@SpringBootApplication
public class ChatAppApplication {

	@Value("${app.timezone:UTC}")
    private String appTimeZone;

	public static void main(String[] args) {
		SpringApplication.run(ChatAppApplication.class, args);
	}

	@PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of(appTimeZone)));
        System.out.println("🌍 Application timezone set to: " + appTimeZone);
    }

}
