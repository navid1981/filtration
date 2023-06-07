package com.github.navid1981.controller;

import com.github.navid1981.service.DemoService;
import com.github.navid1981.service.RequiredAnnotationService;
import com.github.navid1981.service.SchemaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
public class DemoController {

    @Autowired
    private DemoService demoService;

    @Autowired
    private SchemaService schemaService;

    @Autowired
    private RequiredAnnotationService requiredAnnotationService;

    @PostMapping(value = "/schema",consumes = {"application/json"})
    public ResponseEntity<String> getSchema(@RequestBody String payload){
        String schema=demoService.convertJsonToSchema(payload);
        return new ResponseEntity<>(schema, HttpStatus.OK);
    }

    @PostMapping(value = "/schema/req",consumes = {"application/json"})
    public ResponseEntity<String> getSchemaWithRequiredFields(@RequestBody String payload) throws JsonProcessingException, ClassNotFoundException {
        Map<String,Object> map = new ObjectMapper().readValue(payload, HashMap.class);
        Set<String> set=map.keySet();
        for (String key:set) {
            requiredAnnotationService.addRequiredAnnotation(key,map.get(key).toString());
        }
        String result= schemaService.generateSchema();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
