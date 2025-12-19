package com.sonchasapps.models.jpa.notes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyPlanData {
    private String week;
    private List<String> goals;
}
