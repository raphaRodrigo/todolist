package br.com.rapharodrigo.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_tasks")
public class TaskModel {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(length = 50)
    private String title;
    private String description, priority;
    private LocalDateTime startAt, endAt;
    private UUID userId;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
