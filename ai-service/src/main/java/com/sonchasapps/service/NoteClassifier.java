package com.sonchasapps.service;

import com.sonchasapps.llm.LLMClient;
import org.springframework.stereotype.Service;

import java.util.Locale;


@Service
public class NoteClassifier {

    private final LLMClient llmClient;
    private final String classifierModel;

    public NoteClassifier(LLMClient llmClient,
                          @org.springframework.beans.factory.annotation.Value("${llm.classifierModel:qwen-1.5b}") String classifierModel) {
        this.llmClient = llmClient;
        this.classifierModel = classifierModel;
    }

    public String classify(String text) {
        try {
            String prompt = """
                    Классифицируй текст. Верни одно слово из:
                    ["weekly_plan", "project_idea", "todo_list", "journal", "task", "other"].
                    
                    Текст:
                    \"%s\"
                    Ответь ТОЛЬКО ОДНИМ СЛОВОМ.
                    """.formatted(text);
            String resp = llmClient.generate(prompt, classifierModel);
            if (resp != null) {
                String cleaned = resp.strip().toLowerCase(Locale.ROOT).replaceAll("[^a-z_]", "");
                if (isValidType(cleaned)) return cleaned;
            }
        } catch (Exception e) {
        }

        String lower = text.toLowerCase();
        if (lower.contains("недел") || lower.contains("понедельник") || lower.contains("вторник") || lower.contains("пятниц")) return "weekly_plan";
        if (lower.contains("идея") || lower.contains("проект")) return "project_idea";
        if (lower.contains("список") || lower.contains("todo") || lower.contains("сделать")) return "todo_list";
        if (lower.contains("хочу") || lower.contains("чувств") || lower.contains("день")) return "journal";
        if (lower.contains("задач") || lower.contains("задача")) return "task";
        return "other";
    }

    private boolean isValidType(String t) {
        return t.equals("weekly_plan") || t.equals("project_idea") || t.equals("todo_list")
                || t.equals("journal") || t.equals("task") || t.equals("other");
    }
}
