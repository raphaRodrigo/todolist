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

    public void setTitle(String title) throws Exception{
        if (title.length() > 50){
            throw new Exception("The title must not contain over 50 characters.");
        }
        this.title = title;
    }
}
