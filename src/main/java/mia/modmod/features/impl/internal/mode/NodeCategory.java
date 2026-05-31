package mia.modmod.features.impl.internal.mode;

import java.util.List;

public enum NodeCategory {
    MAIN("^node\\d+$", "^beta$"),
    PRIVATE("^private\\d+$"),
    DEV("^dev\\d*$", "^dev.*$", "^build$"),
    MISC_EVENT("^event$", ".*");

    private final String[] matchers;

    NodeCategory(String ...matchers) {
        this.matchers = matchers;
    }

    public String[] getMatchers() { return matchers; }
}