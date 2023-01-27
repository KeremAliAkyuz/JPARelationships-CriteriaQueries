package com.example.jparelationships.mission;

import com.example.jparelationships.employee.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mission")
public class Mission {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private Integer period;

    @ManyToMany(mappedBy = "missions")
    private List<Employee> employees;
}
