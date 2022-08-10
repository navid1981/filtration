package org.navid.model;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;

@Getter
@Setter
public class ConsumerFilter implements Serializable {
    private String rulesExpression;
    private Rule[] rules;
    private String[] eventNames;

    @Override
    public String toString() {
        return "ConsumerFilter{" +
                "rulesExpression='" + rulesExpression + '\'' +
                ", rules=" + Arrays.toString(rules) +
                ", eventNames=" + Arrays.toString(eventNames) +
                '}';
    }
}
