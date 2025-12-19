package com.sonchasapps.models.jpa.notes;

public enum NoteType {
    WEEKLY_PLAN("weekly_plan", "Weekly Plan"),
    PROJECT_IDEA("project_idea", "Project Idea"),
    TODO_LIST("todo_list", "Todo List"),
    JOURNAL("journal", "Journal Entry"),
    TASK("task", "Task"),
    OTHER("other", "Note");

    private final String code;
    private final String displayName;

    NoteType(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static NoteType fromCode(String code) {
        for (NoteType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return OTHER;
    }
}


