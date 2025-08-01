package com.aishwarya.FinBank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.context.annotation.Scope;

import java.util.Map;

@Scope("prototype")
@Entity
@Table(name = "rule")
public class DslRule {

        @Id
        private int id;
        private String name;
//        private Map<String,Object> conditionParams;
//        private Map<String,Object> actionParams;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


//        public Map<String, Object> getConditionParams() {
//            return conditionParams;
//        }
//
//        public void setConditionParams(Map<String, Object> conditionParams) {
//            this.conditionParams = conditionParams;
//        }
//
//        public Map<String, Object> getActionParams() {
//            return actionParams;
//        }
//
//        public void setActionParams(Map<String, Object> actionParams) {
//            this.actionParams = actionParams;
//        }

}
