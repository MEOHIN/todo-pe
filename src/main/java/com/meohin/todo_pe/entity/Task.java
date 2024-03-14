package com.meohin.todo_pe.entity;

import com.meohin.todo_pe.entity.converter.TaskStatusConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@DynamicInsert
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 60)
    private String subject;

    @Column(length = 600)
    private String description;

    private Integer estimatedAt;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @Convert(converter = TaskStatusConverter.class)
    @ColumnDefault("'STANDBY'")
    private TaskStatus status;

    @OneToMany(mappedBy = "task")
    private List<TaskMeasures> taskMeasuresList;

    @ManyToOne
    private SiteUser siteUser;
}
