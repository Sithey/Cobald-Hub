package fr.cobaldhub.games.duel.object;

import com.google.gson.JsonObject;
import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.games.LPlayerStatut;
import fr.cobaldhub.games.arena.object.APlayer;
import fr.cobaldhub.utils.InventoryStringDeSerializer;
import fr.spigot.cobaldapi.API;
import fr.spigot.cobaldapi.objects.JsonValue;
import fr.spigot.cobaldapi.objects.Players;
import fr.spigot.cobaldapi.utils.ItemCreator;
import fr.spigot.cobaldapi.utils.Message;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import java.io.IOException;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class DKit {

    private ItemStack[] inventory, armor;
    private String name, inventorystring, armorstring;
    private Material inventoryitem;
    private boolean bigmap;

    public DKit(String name, String inventory, String armor, Material inventoryitem, boolean bigmap){
        this.name = name;
        this.inventorystring = inventory;
        this.armorstring = armor;
        this.inventoryitem = inventoryitem;
        try {
            this.inventory = InventoryStringDeSerializer.itemStackArrayFromBase64(inventory);
            this.armor = InventoryStringDeSerializer.itemStackArrayFromBase64(armor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.bigmap = bigmap;
        Main.getInstance().getDuel().getKits().add(this);
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public String getInventorystring() {
        return inventorystring;
    }

    public String getArmorstring() {
        return armorstring;
    }

    public String getName() {
        return name;
    }

    public Material getInventoryitem() {
        return inventoryitem;
    }

    public void goToEdit(Player player){
        Location edit = new Location(Bukkit.getWorld("world"), 209.5, 120, 303.5, 90, 2);
        Main.getInstance().getLPlayerByUniqueId(player.getUniqueId()).setEditkit(this);

        player.teleport(edit);

        giveKit(player);
        player.sendMessage(Message.PREFIX.getMessage() + "You are now editing " + getName() + ".");
        player.sendMessage(Message.PREFIX.getMessage() + "Use /saveinv to save your kit");
    }

    public void saveKit(Player player){
        LPlayer p = Main.getInstance().getLPlayerByUniqueId(player.getUniqueId());
        p.setEditkit(null);
        String inv = InventoryStringDeSerializer.itemStackArrayToBase64(player.getInventory().getContents());
        String arm = InventoryStringDeSerializer.itemStackArrayToBase64(player.getInventory().getArmorContents());
        Bson filter = eq("_id", player.getUniqueId().toString());
        JsonValue jsonValue = new JsonValue(p.getPlayers().getDocument());
        JSONObject kito = new JSONObject();
        kito.put("inventory", inv);
        kito.put("armor", arm);
        int elo = jsonValue.getIntValue("hub.kits." + getName() + ".inventory");
        if (elo == 0)
            elo = 1400;
        kito.put("elo", elo);
        Bson kit = set("hub.kits." + getName(), kito);
        API.getInstance().getMongoDB().getPlayers().updateOne(filter, kit);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setWalkSpeed(0.2f);
        player.getInventory().setHeldItemSlot(4);
        player.setGameMode(GameMode.ADVENTURE);
        player.getActivePotionEffects().clear();
        Main.getInstance().getLPlayerByUniqueId(player.getUniqueId()).loadCosmetic();
        player.getInventory().setItem(0, new ItemCreator(Material.SKULL_ITEM).setOwner(player.getName()).setDurability(3).setName(ChatColor.GOLD +"Profile").getItem());
        player.getInventory().setItem(1, new ItemCreator(Main.getInstance().getLPlayerByUniqueId(player.getUniqueId()).isInvisible() ? Material.EYE_OF_ENDER : Material.ENDER_PEARL).setName(ChatColor.GOLD +"Players " +(Main.getInstance().getLPlayerByUniqueId(player.getUniqueId()).isInvisible() ? "Disabled" : "Enabled")).getItem());
        player.getInventory().setItem(2, new ItemCreator(Material.DIAMOND).setName(ChatColor.GOLD +"Donator").getItem());
        player.getInventory().setItem(4, new ItemCreator(Material.COMPASS).setName(ChatColor.GOLD +"Navigation").getItem());
        player.getInventory().setItem(6, new ItemCreator(Material.FEATHER).setName(ChatColor.GOLD +"Parkour").getItem());
        player.getInventory().setItem(7, new ItemCreator(Material.DIAMOND_HELMET).setName(ChatColor.GOLD +"Arena FFA").getItem());
        player.getInventory().setItem(8, new ItemCreator(Material.IRON_SWORD).setName(ChatColor.GOLD +"Duel").getItem());
        for (PotionEffect effect : player.getActivePotionEffects()){
            player.removePotionEffect(effect.getType());
        }
        player.teleport(new Location(Bukkit.getWorld("world"), 209.5, 101.5, 269.5, 0, 0));
    }

    public void giveKit(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 3 * 20, 50, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3 * 20, 50, false, false));
        player.setFireTicks(0);
        Document document = Players.getDocumentByUniqueId(player.getUniqueId().toString());
        JsonValue jsonValue = new JsonValue(document);
        if (jsonValue.getStringValue("hub.kits." + getName() + ".inventory") == null){
            if (!Main.getInstance().getLPlayerByUniqueId(player.getUniqueId()).getStatut().equals(LPlayerStatut.EDITKIT))
                player.sendMessage(Message.PREFIX.getMessage() + "You can edit your kit on the profile menu");
            player.getInventory().setContents(getInventory());
            player.getInventory().setArmorContents(getArmor());
            player.updateInventory();
            return;
        }

        try {
            player.getInventory().setContents(InventoryStringDeSerializer.itemStackArrayFromBase64(jsonValue.getStringValue("hub.kits." + getName() + ".inventory")));
            player.getInventory().setArmorContents(InventoryStringDeSerializer.itemStackArrayFromBase64(jsonValue.getStringValue("hub.kits." + getName() + ".armor") ));
            player.updateInventory();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isBigmap() {
        return bigmap;
    }

    public static DKit getKitByName(String name){
        for (DKit a : Main.getInstance().getDuel().getKits()){
            if (a.getName().equals(name))
                return a;
        }
        return null;
    }
}
