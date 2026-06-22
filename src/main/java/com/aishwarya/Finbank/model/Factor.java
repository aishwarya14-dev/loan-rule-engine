package com.aishwarya.Finbank.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "factor")
@NoArgsConstructor
@Getter
@Setter
public class Factor {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(nullable = false)
        private String name;
        @Column(columnDefinition = "TEXT")
        private String description;
        @Column(name = "is_active" ,nullable = false)
        private Boolean isActive = true;
        @CreationTimestamp
        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;
        @UpdateTimestamp
        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        public Factor(String name, String description) {
            this.name = name;
            this.description = description;
        }
}
