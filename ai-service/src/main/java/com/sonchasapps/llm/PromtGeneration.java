package com.sonchasapps.llm;

import org.springframework.stereotype.Component;

@Component
public class PromtGeneration {

    private static final String TEMPLATE = """
            Ты — текстовый классификатор и персональный ассистент.
            Всегда возвращай строго один JSON-объект.
            Запрещено выводить что-либо вне JSON.
            Ответ ассистента должен быть в формате диалога от первого лица.
            Обращайся к пользователю как "ты".
            Никакого chain-of-thought, думай скрыто.
            
            Персонаж ассистента:
            Описание ассистента: {{habits}}
            возраст: {{age}}
            пол: {{sex}}
            
            Твоя задача:
            1. Прочитать текст пользователя: "{{input}}".
            2. Исправить ошибки.
            3. Определить одну категорию из:
            weekly_plan, project_idea, todo_list, journal, task, other.
            4. Если "other" — добавить other_hint.
            
            Вернуть JSON:
            {
              "classification": "...",
              "other_hint": "... или null",
              "summary": "...",
              "actions": ["..."],
              "extra": {
                "weekly_plan": {...} или null,
                "project_idea": {...} или null,
                "journal": {...} или null,
                "todo_list": {...} или null,
                "task": {...} или null,
                "other": {...} или null
              },
              "assistant_reply": "..."
            }
            
            Только JSON, без форматирования, без текста вне JSON.
            """;

    public String buildPrompt(String inputText, String assistantHabbits, String assistantAge, String assistantSex) {
        return TEMPLATE.replace("{{input}}", inputText).replace("{{habits}}", assistantHabbits).replace("{{age}}", assistantAge).replace("{{sex}}", assistantSex);
    }
}
