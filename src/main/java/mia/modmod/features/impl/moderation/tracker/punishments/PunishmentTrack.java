package mia.modmod.features.impl.moderation.tracker.punishments;

import java.util.List;

public enum PunishmentTrack {
    // these are matched w/ regex non-case-sensitively
    SPAMMING("Spamming", new PunishmentEscalation(ServerPunishmentType.MUTE,3, PunishmentDuration.D1, PunishmentDuration.D3), List.of("Spamming", "Spam", "Join spam")),
    PLOT_AD("Plot Ad Misuse", new PunishmentEscalation(ServerPunishmentType.MUTE,1, PunishmentDuration.D1, PunishmentDuration.D3), List.of("Plot Ad (Misuse|missue|misues)", "plot ad")),
    FILTER_BYPASS("Filter Bypass", new PunishmentEscalation(ServerPunishmentType.MUTE,3, PunishmentDuration.D1, PunishmentDuration.D14), List.of("Filter Bypass", "Bilter Bypass", "bypassing (?:the|) filter", "chat filter bypass", "bypass filter", "swearing")),
    TOXICITY_SUICIDE("Suicide Encouragement", new PunishmentEscalation(ServerPunishmentType.MUTE,0, PunishmentDuration.D7, PunishmentDuration.PERM), List.of("Suicide Encouragement", "Suicide")),
    TOXICITY_HARASSMENT("Harassment", new PunishmentEscalation(ServerPunishmentType.MUTE,0, PunishmentDuration.D7, PunishmentDuration.PERM), List.of("Harassment")),
    TOXICITY_GENERAL_RUDENESS("Toxicity (Rudeness)", new PunishmentEscalation(ServerPunishmentType.MUTE,0, PunishmentDuration.D3, PunishmentDuration.D30), List.of("Toxicity", "Rudeness", "being rude", "rude", "Toxicity (Rudeness)", "Disrespect")),
    DISCRIMINATION("Discrimination", new PunishmentEscalation(ServerPunishmentType.MUTE,0, PunishmentDuration.D14, PunishmentDuration.PERM), List.of("Discriminat(?:ion|)", "Slurs", "Discrimination (?:/|-|&) (Filter Bypass|Bilter|Bilter Bypass|Filter Flypass)", "(Trans|Xeno|Homo)phobia", "Anti(|-)semitism", "n(|-)word", "racism", "racist")),
    BANNED_TOPICS("Banned Topics", new PunishmentEscalation(ServerPunishmentType.MUTE,0, PunishmentDuration.D7, PunishmentDuration.PERM), List.of("Banned Topics", "Politics")),

    // this needs to be before so it catches extremely last
    INAPPROPRIATE_CHAT("Inappropriate Chat Messages", new PunishmentEscalation(ServerPunishmentType.MUTE,0, PunishmentDuration.D7, PunishmentDuration.PERM), List.of("^Inappropriate (Chat|Topic|message)")),
    SEVERELY_INAPPROPRIATE_CHAT("Extremely Inappropriate Chat Messages", new PunishmentEscalation(ServerPunishmentType.MUTE,0, PunishmentDuration.D30, PunishmentDuration.PERM), List.of("(Severely|Extremely|Very|Disturbing|explicit) Inappropriate (Chat|Topics|message)", "pedo(|philia)", "Sexual", "creepy")),

    HACKED_CLIENT("Hacked Client", new PunishmentEscalation(ServerPunishmentType.BAN,0, PunishmentDuration.D30, PunishmentDuration.PERM), List.of("Hacked Client", "Reach", "Movement Hacks", "Hacks", "Hacking", "Criticals", "Bhop", "Kill(|-| |_)aura", "Aimbot", "flying", "spider")),
    MACROING("Macroing / Autoclicking", new PunishmentEscalation(ServerPunishmentType.BAN,0, PunishmentDuration.D3, PunishmentDuration.PERM), List.of("Macroing", "Autoclicking", "anti( |-)afk", "autoclick", "automatic")),
    MALICIOUS_ITEMS("Malicious Items", new PunishmentEscalation(ServerPunishmentType.BAN,0, PunishmentDuration.D3, PunishmentDuration.PERM), List.of("Malicious Items", "Crash (Items|Shulker)", "Chunk Banning", "Crashing players")),
    INFORMATION_MODS("Disallowed Information Mods", new PunishmentEscalation(ServerPunishmentType.BAN,0, PunishmentDuration.D3, PunishmentDuration.PERM), List.of("Xray", "Information Mods", "Informational Mods", "Disallowed Information Mods", "Visible Barriers")),
    CLIENT_EXPLOITING("Client Exploit Abuse", new PunishmentEscalation(ServerPunishmentType.BAN,0, PunishmentDuration.D7, PunishmentDuration.PERM), List.of("Tabbing", "Client Exploits", "client exploit abuse", "abusing client exploits")),

    SERVER_CRASHING("Intentional Server Crashing", new PunishmentEscalation(ServerPunishmentType.BAN,0, PunishmentDuration.PERM, PunishmentDuration.PERM), List.of("Crashing (Server|Node)", "Server (Crash|Crashing|Crashes|Node)", "Intentionally Crashing (Server|Node)", "Intentional (Server|Node) Crashing", "Exploit Abuse")),

    INAPPROPRIATE_SKIN_USERNAME("Inappropriate Skin / Username (Appeal when changed)", new PunishmentEscalation(ServerPunishmentType.BAN,0, PunishmentDuration.PERM, PunishmentDuration.PERM), List.of("(Inappropriate|explicit|disallowed|banned) Skin", "(Inappropriate|explicit|disallowed|banned) (|User)Name")),
    BAN_EVASION("Ban Evasion", new PunishmentEscalation(ServerPunishmentType.BAN,0, PunishmentDuration.PERM, PunishmentDuration.PERM), List.of("Ban Evasion")),
    BOT_ACCOUNT("Bot Account / compromised Account", new PunishmentEscalation(ServerPunishmentType.BAN,0, PunishmentDuration.PERM, PunishmentDuration.PERM), List.of("Bot Account", "compromised")),

    // Requires custom duration input

    INAPPROPRIATE_PLOT_CONTENT("Inappropriate Plot Content", new PunishmentEscalation(ServerPunishmentType.BAN,0, PunishmentDuration.PERM, PunishmentDuration.PERM), List.of("(inappropriate|explicit|sexual) plot content")),
    MUTE_EVASION("Mute Evasion", new PunishmentEscalation(ServerPunishmentType.BAN,0, PunishmentDuration.PERM, PunishmentDuration.PERM), List.of("Mute Evasion", "Mute Bypass", "bypassing mute", "bypass mute", "mute evasion"));

    public static final List<PunishmentTrack> expiringPunishments = List.of(
            SPAMMING,
            PLOT_AD,
            FILTER_BYPASS,
            TOXICITY_GENERAL_RUDENESS
    );

    private final String reasonText;
    private final PunishmentEscalation punishmentEscalation;
    private final List<String> patterns;

    PunishmentTrack(String reasonText, PunishmentEscalation punishmentEscalation, List<String> patterns) {
        this.reasonText = reasonText;
        this.punishmentEscalation = punishmentEscalation;
        this.patterns = patterns;
    }

    public String getReasonText() { return reasonText; }
    public PunishmentEscalation getPunishmentEscalation() { return punishmentEscalation; }
    public List<String> getPatterns() { return patterns; }
}

