package com.calendiary.calendiary_backend.model;

import com.calendiary.calendiary_backend.dto.LabelCreateDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "name"})
})
@Getter
@Setter
@NoArgsConstructor
public class LabelEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private Long userId;

    public LabelEntity(LabelCreateDTO dto, Long userId) {
        name = dto.name();
        color = dto.color();
        this.userId = userId;
    }
}
