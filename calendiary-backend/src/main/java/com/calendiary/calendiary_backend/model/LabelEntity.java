package com.calendiary.calendiary_backend.model;

import com.calendiary.calendiary_backend.dto.LabelCreateDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "labels", // It's good practice to explicitly name the table
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "name"})
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabelEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private Long userId;

    public LabelEntity(String name) {
        this.name = name;
    }
}
