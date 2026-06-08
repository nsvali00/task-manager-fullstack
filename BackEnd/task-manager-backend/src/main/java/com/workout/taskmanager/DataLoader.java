package com.workout.taskmanager;

import com.workout.taskmanager.task.entity.Task;
import com.workout.taskmanager.task.repository.TaskRepository;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

        public DataLoader(TaskRepository taskRepository) {
            taskRepository.save(new Task("Learn Spring Boot", "Spring boot task for learning", true));
            taskRepository.save(new Task("Build React UI", "React UI task", false));
        }

}
