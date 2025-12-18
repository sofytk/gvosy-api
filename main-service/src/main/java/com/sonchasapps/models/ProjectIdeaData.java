package com.sonchasapps.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectIdeaData {
    private String projectName;
    private String description;
    private List<String> steps;
}
