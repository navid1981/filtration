package io.github.navid1981.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import io.github.navid1981.model.ConsumerFilter;
import io.github.navid1981.model.Rule;
import io.github.navid1981.model.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
/**
 *Filter Service class:
 *
 *Each rules in the below structure can look at the below payload(search all 'People' array) to match with it.
 * •	name: is name of the rule which is used by rulesExpression.
 * •	path: is payload path which your desired field is located
 * •	key: The field which we want to check its value for rule/filter
 * •	value: desired value
 * •	condition: relation between key and value. Can be: 'eq', 'Contains','lt' (Less Than), 'gt' (greater than), 'empty', 'null', 'exist', 'true', 'false', 'regex', 'in'.
 * In the below example rules, A and C return True but rule B return False. So, in ruleExpression we have True && (False || True) which is totally return True. Therefore Payload pass filter and can be consumed by application.
 *
 * Rules Structure:
 * {
 *   "rules": [
 *     {
 *       "name": "A",
 *       "path": "people.[*].Address.[?]",
 *       "key": "city",
 *       "value": "Chicago",
 *       "condition": "eq"
 *     },
 *     {
 *       "name": "B",
 *       "path": "people.[*].Address.[?]",
 *       "key": "state",
 *       "value": "TX",
 *       "condition": "eq"
 *     },
 *     {
 *       "name": "C",
 *       "path": "people.[*].Address.[?]",
 *       "key": "state",
 *       "value": "VA",
 *       "condition": "eq"
 *     }
 *   ],
 *   "rulesExpression": "A && ( B || C )"
 * }
 *
 * Payload:
 * {
 *   "people": [
 *     {
 *       "name": "Ali",
 *       "address": {
 *         "street": "1211 W Roosevelt Rd",
 *         "city": "Chicago",
 *         "state": "IL",
 *         "zipcode": "60608",
 *         "counrty": "US"
 *       }
 *     },
 *     {
 *       "name": "David",
 *       "address": {
 *         "street": "1211 Lee Highway",
 *         "city": "Fairfax",
 *         "state": "VA",
 *         "zipcode": "50608",
 *         "counrty": "US"
 *       }
 *     },
 *     {
 *       "name": "Jack",
 *       "address": {
 *         "street": "1011 E Main Dr",
 *         "city": "Newyork",
 *         "state": "NY",
 *         "zipcode": "60208",
 *         "counrty": "US"
 *       }
 *     }
 *   ]
 * }
 * @author  Navid Salehvaziri
 */
@Service
public class FilterService {

    private static final Logger LOG = LoggerFactory.getLogger(FilterService.class);

    ObjectMapper mapper = new ObjectMapper();

    /**
     *
     * @param message xml or json payload.
     * @param filter filter structure.
     * @param type xml/json.
     * @return true if filter match payload. False if it does not.
     */
    public boolean hasFilter(String message, String filter, Type type) {
        if (type == Type.XML) {
            XmlMapper xmlMapper = new XmlMapper();
            try {
                JsonNode node = xmlMapper.readTree(message.getBytes());
                message = mapper.writeValueAsString(node);
            } catch (IOException e) {
                LOG.error("Cannot convert xml to json. " + e.getMessage());
            }
        }
            ConsumerFilter myFilter;

            if (filter.isEmpty()) return true;

            try {
                myFilter = mapper.readValue(filter, ConsumerFilter.class);
            } catch (IOException e) {
                LOG.error("Error in mapping json to filter: " + e.getMessage() + " filter is: " + filter);
                return false;
            }
            LOG.info(myFilter.toString());


            Rule[] rules = myFilter.getRules();
            if (rules.length == 0) return true;

            Map<String, Boolean> map = new HashMap<>();
            for (int i = 0; i < rules.length; i++) {
                map.put(rules[i].getName(), hasRule(message, rules[i].getPath(), rules[i].getKey(), rules[i].getValue(), rules[i].getCondition()));
            }

            ScriptEngineManager sem = new ScriptEngineManager();
            ScriptEngine se = sem.getEngineByName("JavaScript");
            String[] expression = myFilter.getRulesExpression().split(" ");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < expression.length; i++) {
                if (!expression[i].equals("&&") && !expression[i].equals("||") && !expression[i].equals("(") && !expression[i].equals(")")) {
                    Boolean value = map.get(expression[i]);
                    if (value == null) {
                        LOG.error("FilterExpression is not correct. Expression: " + myFilter.getRulesExpression() + "Rules name are: " + map.keySet().stream().toArray());
                        return false;
                    }
                    sb.append(value);
                    continue;
                }
                sb.append(expression[i]);
            }
            Object result = false;
            try {
                result = se.eval(sb.toString());
            } catch (ScriptException e) {
                LOG.error("Wrong filterExpression: " + myFilter.getRulesExpression() + e.getMessage());
                return false;
            }
            return Boolean.valueOf(result.toString());
        }

    private boolean hasRule(String eventMessage,String path, String key, String value, String condition){
        Filter filter = null;
        if(condition.equals("contains")){
            filter = Filter.filter(Criteria.where(key).contains(value));
        }else if(condition.equals("lt")){
            int v=Integer.parseInt(value);
            filter = Filter.filter(Criteria.where(key).lt(v));
        }else if(condition.equals("gt")){
            int v=Integer.parseInt(value);
            filter = Filter.filter(Criteria.where(key).gt(v));
        }else if(condition.equals("eq")){
            filter = Filter.filter(Criteria.where(key).eq(value));
        }else if(condition.equals("empty")){
            filter = Filter.filter(Criteria.where(key).empty(true));
        }else if(condition.equals("ne")){
            filter = Filter.filter(Criteria.where(key).ne(value));
        }else if(condition.equals("null")){
            filter = Filter.filter(Criteria.where(key).eq(null));
        }else if(condition.equals("exist")){
            if(value.equalsIgnoreCase("true")) {
                filter = Filter.filter(Criteria.where(key).eq(true));
            }else if(value.equalsIgnoreCase("false")){
                filter = Filter.filter(Criteria.where(key).eq(false));
            }
        }else if(condition.equals("regex")){
            Pattern pattern=Pattern.compile(value);
            filter = Filter.filter(Criteria.where(key).regex(pattern));
        }else if(condition.equals("in")){
            String[] values=value.split("-");
            List<String> list=Arrays.asList(values);
            filter = Filter.filter(Criteria.where(key).in(list));
        }else{
            LOG.error("Wrong Condition set for filter");
            return false;
        }

        List<Map<String, Object>> list;
        try{
            list = JsonPath.parse(eventMessage).read("$."+path,filter);
        }catch (PathNotFoundException e){
            LOG.error("JSON path is wrong");
            return false;
        }
        if(list.size()>0){
            return true;
        }else{
            return false;
        }
    }

}

