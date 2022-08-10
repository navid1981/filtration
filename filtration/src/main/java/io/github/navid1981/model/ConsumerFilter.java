package io.github.navid1981.model;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;

@Getter
@Setter
public class ConsumerFilter implements Serializable {
    private String rulesExpression;
    private Rule[] rules;

    @Override
    public String toString() {
        return "ConsumerFilter{" +
                "rulesExpression='" + rulesExpression + '\'' +
                ", rules=" + Arrays.toString(rules) +
                '}';
    }
}

