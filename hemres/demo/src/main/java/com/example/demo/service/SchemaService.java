package com.example.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.victools.jsonschema.generator.*;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jackson.JacksonOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLClassLoader;

@Service
public class SchemaService {

    @Autowired
    private URLClassLoader urlClassLoader;

    @Value("${java.package}")
    private String packageName;

    public String generateSchema() throws ClassNotFoundException {
        SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_7, OptionPreset.PLAIN_JSON);
        JacksonModule module = new JacksonModule(JacksonOption.RESPECT_JSONPROPERTY_REQUIRED);
        SchemaGeneratorConfig config = configBuilder.with(module).build();

        SchemaGenerator generator = new SchemaGenerator(config);
        Class<?> loadedClass = urlClassLoader.loadClass(packageName+".PublisherPayload");
        JsonNode jsonSchema = generator.generateSchema(loadedClass);

        return jsonSchema.toPrettyString();
    }
}
