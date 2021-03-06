package com.playmonumenta.scriptedquests.quests.components.actions;

import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.playmonumenta.scriptedquests.Plugin;
import com.playmonumenta.scriptedquests.quests.components.QuestPrerequisites;

public class ActionInteractNpc implements ActionBase {
	private String mName;
	private EntityType mType = EntityType.VILLAGER;

	public ActionInteractNpc(JsonElement element) throws Exception {
		JsonObject object = element.getAsJsonObject();
		if (object == null) {
			throw new Exception("interact_npc value is not an object!");
		}

		Set<Entry<String, JsonElement>> entries = object.entrySet();
		for (Entry<String, JsonElement> ent : entries) {
			String key = ent.getKey();

			if (key.equals("name")) {
				try {
					mName = ent.getValue().getAsString();
				} catch (IllegalArgumentException e) {
					throw new Exception("interact_npc name is not a string!");
				}
			} else if (key.equals("entity_type")) {
				try {
					mType = EntityType.valueOf(ent.getValue().getAsString());
				} catch (IllegalArgumentException e) {
					throw new Exception("Invalid entity_type! Must exactly match one of Spigot's EntityType values.");
				}
			} else {
				throw new Exception("Unknown interact_npc key: '" + key + "'");
			}
		}
	}

	@Override
	public void doAction(Plugin plugin, Player player, Entity npcEntity, QuestPrerequisites prereqs) {
		/*
		 * Important - we are switching the context here to a different entity - so we don't pass
		 * npcEntity into that interaction
		 */
		if (!plugin.mNpcManager.interactEvent(plugin, player, mName, mType, null, true)) {
			plugin.getLogger().severe("No interaction available for player '" + player.getName() +
			                          "' and NPC '" + mName + "'");
		}
	}
}

