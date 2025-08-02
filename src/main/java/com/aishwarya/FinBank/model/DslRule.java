package com.aishwarya.FinBank.model;

import com.aishwarya.FinBank.ruleengine.model.Action;
import com.aishwarya.FinBank.ruleengine.model.Condition;
import com.aishwarya.FinBank.ruleengine.model.Logic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.context.annotation.Scope;

@Scope("prototype")
@Entity
@Table(name = "rule")
public class DslRule {

        @Id
        private int id;
        private String type;
        private String logic;
        @Column(columnDefinition = "JSON")
        private Condition condition;
        private Action action;


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }


        public String getLogic() {
          return logic;
        }


        public Condition getCondition() {
          return condition;
        }

        public Action getAction() {
          return action;
        }
}
