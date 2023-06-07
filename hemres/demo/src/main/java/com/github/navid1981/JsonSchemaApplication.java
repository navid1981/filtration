package com.github.navid1981;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

@SpringBootApplication
public class JsonSchemaApplication {
    @Value("${java.model.path}")
    private String path;

    public static void main(String[] args){
        SpringApplication.run(JsonSchemaApplication.class,args);
    }

    @Bean("urlClassLoader")
    public URLClassLoader getURLClassLoader(){
        try {
            return new URLClassLoader(new URL[]{new File(path).toURI().toURL()});
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Bean("file")
    public File getFile(){
        return new File(path);
    }
}
