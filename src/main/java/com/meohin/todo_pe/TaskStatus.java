package com.meohin.todo_pe;

import lombok.Getter;

@Getter
public enum TaskStatus {
    STANDBY("STANDBY"),
    ING("ING"),
    PAUSE("PAUSE");

    private String value;

    TaskStatus(String value) {
        this.value = value;
    }
}
