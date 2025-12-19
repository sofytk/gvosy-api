package com.sonchasapps.models.jpa.notes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalData {
    private String date;
    private String mood;
    private List<String> entries;
}
