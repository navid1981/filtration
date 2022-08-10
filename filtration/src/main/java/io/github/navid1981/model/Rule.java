package io.github.navid1981.model;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Rule implements Serializable {
    private String name;
    private String path;
    private String key;
    private String value;
    private String condition;

    @Override
    public String toString() {
        return "Rule{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", condition='" + condition + '\'' +
                '}';
    }
}

