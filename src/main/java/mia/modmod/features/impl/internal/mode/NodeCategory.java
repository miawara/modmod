package mia.modmod.features.impl.internal.mode;

import mia.modmod.ColorBank;

public enum NodeCategory {
    MAIN("Main Nodes", 0x85ff91 ,"^node\\d+$", "^beta$"),
    PRIVATE("Private Nodes",0x85caff,"^private\\d+$"),
    DEV("Dev Nodes", 0xb28fff,"^dev\\d*$", "^dev.*$", "^build$"),
    MISC_EVENT("Event Nodes",ColorBank.WHITE_GRAY, ".*");

    private final String name;
    private final int color;

    private final String[] matchers;

    NodeCategory(String name, int color, String ...matchers) {
        this.name = name;
        this.color = color;
        this.matchers = matchers;
    }

    public String getName() { return name; }
    public int getColor() { return color; }
    public String[] getMatchers() { return matchers; }
}