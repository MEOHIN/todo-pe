package com.meohin.todo_pe.entity;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TaskStatus {
    STANDBY("STANDBY"),
    ING("ING"),
    PAUSE("PAUSE");

    private String value;

    TaskStatus(String value) {
        this.value = value;
    }

    public static TaskStatus ofCode(String dbData) {
        return Arrays.stream(TaskStatus.values())
                .filter(v -> v.getValue().equals(dbData))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Task 상태에 %s가 존재하지 않습니다.", dbData)));
    }
}
