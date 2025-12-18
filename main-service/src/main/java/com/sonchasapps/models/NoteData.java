package com.sonchasapps.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteData {

    private NoteType classification;
    private String otherHint;
    private String summary;
    private List<String> actions;
    private String assistantReply;

    private WeeklyPlanData weeklyPlan;
    private ProjectIdeaData projectIdea;
    private JournalData journal;
    private TodoListData todoList;
    private TaskData task;
    private OtherData other;


    public void parse(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            this.classification = NoteType.OTHER;
            return;
        }

        try {
            JSONObject json = new JSONObject(jsonString);

            this.classification = NoteType.fromCode(json.optString("classification", "other"));
            this.otherHint = json.optString("other_hint", null);
            this.summary = json.optString("summary", "");
            this.assistantReply = json.optString("assistant_reply", "");

            this.actions = new ArrayList<>();
            if (json.has("actions")) {
                JSONArray actionsArray = json.getJSONArray("actions");
                for (int i = 0; i < actionsArray.length(); i++) {
                    this.actions.add(actionsArray.getString(i));
                }
            }

            if (json.has("extra")) {
                JSONObject extra = json.getJSONObject("extra");

                if (extra.has("weekly_plan") && !extra.isNull("weekly_plan")) {
                    this.weeklyPlan = parseWeeklyPlan(extra.getJSONObject("weekly_plan"));
                }

                if (extra.has("project_idea") && !extra.isNull("project_idea")) {
                    this.projectIdea = parseProjectIdea(extra.getJSONObject("project_idea"));
                }

                if (extra.has("journal") && !extra.isNull("journal")) {
                    this.journal = parseJournal(extra.getJSONObject("journal"));
                }

                if (extra.has("todo_list") && !extra.isNull("todo_list")) {
                    this.todoList = parseTodoList(extra.getJSONObject("todo_list"));
                }

                if (extra.has("task") && !extra.isNull("task")) {
                    this.task = parseTask(extra.getJSONObject("task"));
                }

                if (extra.has("other") && !extra.isNull("other")) {
                    this.other = parseOther(extra.getJSONObject("other"));
                }
            }

        } catch (Exception e) {
            System.err.println("Error parsing LLM response: " + e.getMessage());
            e.printStackTrace();
            this.classification = NoteType.OTHER;
        }
    }

    private WeeklyPlanData parseWeeklyPlan(JSONObject json) {
        WeeklyPlanData data = new WeeklyPlanData();
        data.setWeek(json.optString("week", null));
        data.setGoals(jsonArrayToList(json.optJSONArray("goals")));
        return data;
    }

    private ProjectIdeaData parseProjectIdea(JSONObject json) {
        ProjectIdeaData data = new ProjectIdeaData();
        data.setProjectName(json.optString("project_name", null));
        data.setDescription(json.optString("description", null));
        data.setSteps(jsonArrayToList(json.optJSONArray("steps")));
        return data;
    }

    private JournalData parseJournal(JSONObject json) {
        JournalData data = new JournalData();
        data.setDate(json.optString("date", null));
        data.setMood(json.optString("mood", null));
        data.setEntries(jsonArrayToList(json.optJSONArray("entries")));
        return data;
    }

    private TodoListData parseTodoList(JSONObject json) {
        TodoListData data = new TodoListData();
        data.setItems(jsonArrayToList(json.optJSONArray("items")));
        return data;
    }

    private TaskData parseTask(JSONObject json) {
        TaskData data = new TaskData();
        data.setTaskName(json.optString("task_name", null));
        data.setDeadline(json.optString("deadline", null));
        data.setPriority(json.optString("priority", null));
        return data;
    }

    private OtherData parseOther(JSONObject json) {
        OtherData data = new OtherData();
        data.setNotes(json.optString("notes", null));
        return data;
    }

    private List<String> jsonArrayToList(JSONArray array) {
        List<String> list = new ArrayList<>();
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                list.add(array.getString(i));
            }
        }
        return list;
    }


    public String getMetadataJson() {
        try {
            JSONObject metadata = new JSONObject();

            if (weeklyPlan != null) metadata.put("weekly_plan", weeklyPlanToJson(weeklyPlan));
            if (projectIdea != null) metadata.put("project_idea", projectIdeaToJson(projectIdea));
            if (journal != null) metadata.put("journal", journalToJson(journal));
            if (todoList != null) metadata.put("todo_list", todoListToJson(todoList));
            if (task != null) metadata.put("task", taskToJson(task));
            if (other != null) metadata.put("other", otherToJson(other));

            if (actions != null && !actions.isEmpty()) {
                metadata.put("actions", new JSONArray(actions));
            }

            if (otherHint != null) {
                metadata.put("other_hint", otherHint);
            }

            return metadata.toString();

        } catch (Exception e) {
            return "{}";
        }
    }

    private JSONObject weeklyPlanToJson(WeeklyPlanData data) {
        JSONObject json = new JSONObject();
        if (data.getWeek() != null) json.put("week", data.getWeek());
        if (data.getGoals() != null) json.put("goals", new JSONArray(data.getGoals()));
        return json;
    }

    private JSONObject projectIdeaToJson(ProjectIdeaData data) {
        JSONObject json = new JSONObject();
        if (data.getProjectName() != null) json.put("project_name", data.getProjectName());
        if (data.getDescription() != null) json.put("description", data.getDescription());
        if (data.getSteps() != null) json.put("steps", new JSONArray(data.getSteps()));
        return json;
    }

    private JSONObject journalToJson(JournalData data) {
        JSONObject json = new JSONObject();
        if (data.getDate() != null) json.put("date", data.getDate());
        if (data.getMood() != null) json.put("mood", data.getMood());
        if (data.getEntries() != null) json.put("entries", new JSONArray(data.getEntries()));
        return json;
    }

    private JSONObject todoListToJson(TodoListData data) {
        JSONObject json = new JSONObject();
        if (data.getItems() != null) json.put("items", new JSONArray(data.getItems()));
        return json;
    }

    private JSONObject taskToJson(TaskData data) {
        JSONObject json = new JSONObject();
        if (data.getTaskName() != null) json.put("task_name", data.getTaskName());
        if (data.getDeadline() != null) json.put("deadline", data.getDeadline());
        if (data.getPriority() != null) json.put("priority", data.getPriority());
        return json;
    }

    private JSONObject otherToJson(OtherData data) {
        JSONObject json = new JSONObject();
        if (data.getNotes() != null) json.put("notes", data.getNotes());
        return json;
    }
}
