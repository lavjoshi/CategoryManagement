package com.CategoryManagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(unique = true)
    String name;

    @Column
    @JsonIgnore
    Integer leftValue;

    @Column
    @JsonIgnore
    Integer rightValue;


    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", left_category=" + leftValue +
                ", right_category=" + rightValue +
                '}';
    }
}
