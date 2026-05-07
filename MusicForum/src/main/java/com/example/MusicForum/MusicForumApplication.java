package com.example.MusicForum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class MusicForumApplication {

	public static void main(String[] args) {
		System.out.println("hola mundo");
		SpringApplication.run(MusicForumApplication.class, args);
	}

}
