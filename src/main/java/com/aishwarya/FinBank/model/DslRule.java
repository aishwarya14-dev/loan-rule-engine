package com.aishwarya.FinBank.model;

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
        private String type;
        private String logic;
        @Type(JsonBinaryType.class)
        @Column(columnDefinition = "jsonb")
        private Condition condition;
        private String action;

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

        public String getAction() {
          return action;
        }
}
