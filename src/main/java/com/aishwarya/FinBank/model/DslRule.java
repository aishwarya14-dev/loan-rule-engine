package com.aishwarya.FinBank.model;

import com.aishwarya.FinBank.ruleengine.model.Action;
import com.aishwarya.FinBank.ruleengine.model.Logic;
import com.aishwarya.FinBank.ruleengine.model.Rule;
import com.aishwarya.FinBank.ruleengine.model.RuleType;
import com.aishwarya.FinBank.ruleengine.model.condition.Condition;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Type;
import org.springframework.context.annotation.Scope;

@Scope("prototype")
@Entity
@Table(name = "rule")
public class DslRule {
        @Id
        private int id;
        private RuleType type;
        private Logic logic;
        @Type(JsonBinaryType.class)
        @Column(columnDefinition = "jsonb")
        private Condition condition;
        private Action action;

        public RuleType getType() {
            return type;
        }

        public void setType(RuleType type) {
            this.type = type;
        }

        public Logic getLogic() {
          return logic;
        }

        public Condition getCondition() {
          return condition;
        }

        public Action getAction() {
          return action;
        }
}
