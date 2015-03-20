package com.trc202.settings;


import com.google.common.collect.Lists;

import java.util.ArrayList;

import lombok.*;

@Getter
@Setter
@Deprecated
public class Settings {
    private boolean stopCombatSafezoning;
    private int tagDuration;
    private boolean debugEnabled;
    private boolean instaKill;
    private ArrayList<String> disabledCommands;
    private ArrayList<String> disallowedWorlds;
    private String npcName;
    private boolean blockEditWhileTagged;
    private boolean sendMessageWhenTagged;
    private int npcDespawnTime;
    private boolean npcDieAfterTime;
    private boolean dropTagOnKick;
    private String commandMessageTagged;
    private String commandMessageNotTagged;
    private String tagMessageDamager;
    private String tagMessageDamaged;
    private boolean blockTeleport;
    private boolean blockEnderPearl;
    private boolean dontSpawnInWG;
    private boolean onlyDamagerTagged;
    private boolean mobTag;
    private boolean playerTag;
    private boolean blockCreativeTagging;
    private boolean blockFly;
    private boolean updateEnabled;
    private boolean disableDisguisesInCombat;

    public Settings() {
        disableDisguisesInCombat = true;
        stopCombatSafezoning = true;
        updateEnabled = true;
        instaKill = false;
        tagDuration = 10;
        debugEnabled = false;
        disabledCommands = Lists.newArrayList();
        disallowedWorlds = Lists.newArrayList();
        npcName = "PvpLogger";
        blockEditWhileTagged = true;
        sendMessageWhenTagged = false;
        npcDespawnTime = -1;
        npcDieAfterTime = false;
        dropTagOnKick = true;
        commandMessageTagged = "You are in combat for [time] seconds.";
        commandMessageNotTagged = "You are not currently in combat.";
        tagMessageDamager = "You have hit [player]. Type /ct to check your remaining tag time.";
        tagMessageDamaged = "You have been hit by [player]. Type /ct to check your remaining tag time.";
        blockTeleport = false;
        blockEnderPearl = false;
        dontSpawnInWG = false;
        onlyDamagerTagged = false;
        mobTag = false;
        playerTag = true;
        blockCreativeTagging = true;
        blockFly = false;
    }
}