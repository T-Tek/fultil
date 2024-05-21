package com.fultil.enums;

import lombok.Getter;

@Getter
public enum EmailTemplateName {
    ACTIVATE_ACCOUNT("activate account");

    EmailTemplateName(String name) {
        this.name = name;
    }

    private final String name;
}
