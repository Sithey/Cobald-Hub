package fr.cobaldhub.games.duel;

import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.games.duel.object.*;
import fr.spigot.cobaldapi.utils.ItemCreator;
import fr.spigot.cobaldapi.utils.Message;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Duel {

    private List<DMap> maps;
    private List<DKit> kits;
    private Map<DKit, List<LPlayer>> queueranked;
    public Duel(){
        this.maps = new ArrayList<>();
        this.kits = new ArrayList<>();
        this.queueranked = new HashMap<>();
        new BukkitRunnable() {
            @Override
            public void run() {
                new DMap("Dojo", new Location(Bukkit.getWorld("duel"), 92.5, 100, 92.5, -44, 0), new Location(Bukkit.getWorld("duel"), 108.5, 100, 108.5, 134, 0), new Location(Bukkit.getWorld("duel"), 115, 113, 85), new Location(Bukkit.getWorld("duel"), 85, 96, 115), false);
                new DMap("Dojo", new Location(Bukkit.getWorld("duel"), -20.5, 100, 98.5, 42, 0), new Location(Bukkit.getWorld("duel"), -36.5, 100, 114.5, -130, 0), new Location(Bukkit.getWorld("duel"), -11, 114, 89), new Location(Bukkit.getWorld("duel"), -45, 94, 124), false);
                new DMap("Dojo", new Location(Bukkit.getWorld("duel"), -34.5, 99, -57.5, -135, 0), new Location(Bukkit.getWorld("duel"), -18.5, 99, -73.5, 45, 0), new Location(Bukkit.getWorld("duel"), -9, 113, -83), new Location(Bukkit.getWorld("duel"), -45, 93, -47), false);
                new DMap("Dojo", new Location(Bukkit.getWorld("duel"), 116.5, 96, -41.5, -135, 0), new Location(Bukkit.getWorld("duel"), 132.5, 96, -57.5, 45, 0), new Location(Bukkit.getWorld("duel"), 142, 110, -67), new Location(Bukkit.getWorld("duel"), 104, 90, -29), false);
           //     new DMap("Dojo", new Location(Bukkit.getWorld("duel"), 116.5, 96, -41.5, -135, 0), new Location(Bukkit.getWorld("duel"), 132.5, 96, -57.5, 45, 0), new Location(Bukkit.getWorld("duel"), 142, 110, -67), new Location(Bukkit.getWorld("duel"), 104, 90, -29), false);

                new DMap("Aquarium", new Location(Bukkit.getWorld("duel"), 997.5, 101, 991.5, -16, 0), new Location(Bukkit.getWorld("duel"), 1003.5, 101, 1006.5, 163, 0), new Location(Bukkit.getWorld("duel"), 988, 99, 1012), new Location(Bukkit.getWorld("duel"), 1012, 134, 988), false);
                new DMap("Aquarium", new Location(Bukkit.getWorld("duel"), 1000.5, 101, 1121.5, -45, 0), new Location(Bukkit.getWorld("duel"), 1010.5, 101, 1131.5, 135, 0), new Location(Bukkit.getWorld("duel"), 989, 138, 1142), new Location(Bukkit.getWorld("duel"), 1024, 95, 1109), false);
                new DMap("Aquarium", new Location(Bukkit.getWorld("duel"), 893.5, 103, 1105.5, 135, 0), new Location(Bukkit.getWorld("duel"), 878.5, 103, 1094.5, -45, 0), new Location(Bukkit.getWorld("duel"), 868, 139, 1115), new Location(Bukkit.getWorld("duel"), 900, 95, 1082), false);
                new DMap("Aquarium", new Location(Bukkit.getWorld("duel"), 853.5, 90, 927.5, -45, 0), new Location(Bukkit.getWorld("duel"), 864.5, 90, 937.5, 135, 0), new Location(Bukkit.getWorld("duel"), 843, 126, 948), new Location(Bukkit.getWorld("duel"), 876, 83, 915), false);
                new DMap("Aquarium", new Location(Bukkit.getWorld("duel"), 961.5, 93, 835.5, 135, 0), new Location(Bukkit.getWorld("duel"), 950.5, 93, 824.5, -45, 0), new Location(Bukkit.getWorld("duel"), 940, 130, 845), new Location(Bukkit.getWorld("duel"), 973, 87, 812), false);

                new DMap("Guarden",new Location(Bukkit.getWorld("duel"), 1969.5, 100, 2008.5, -135, 0) ,new Location(Bukkit.getWorld("duel"), 2012.5, 100, 1965.5, 45, 0) ,new Location(Bukkit.getWorld("duel"), 2015, 98, 1962) , new Location(Bukkit.getWorld("duel"), 1965, 116, 2011), true);
                new DMap("Guarden",new Location(Bukkit.getWorld("duel"), 2104.5, 100, 1879.5, 45, 0) ,new Location(Bukkit.getWorld("duel"), 2060.5, 100, 1910.5, -135, 0) ,new Location(Bukkit.getWorld("duel"), 2058, 116, 1867) , new Location(Bukkit.getWorld("duel"), 2111, 96, 1924), true);
                new DMap("Guarden",new Location(Bukkit.getWorld("duel"), 2131.5, 95, 2053.5, 45, 0) ,new Location(Bukkit.getWorld("duel"), 2094.5, 95, 2084.5, -135, 0) ,new Location(Bukkit.getWorld("duel"), 2085, 111, 2045) , new Location(Bukkit.getWorld("duel"), 2139, 90, 2098), true);
                new DMap("Guarden",new Location(Bukkit.getWorld("duel"), 2019.5, 85, 2105.5, 45, 0) ,new Location(Bukkit.getWorld("duel"), 1980.5, 85, 2140.5, -135, 0) ,new Location(Bukkit.getWorld("duel"), 2027, 80, 2148) , new Location(Bukkit.getWorld("duel"), 1972, 101, 2095), true);
                new DMap("Guarden",new Location(Bukkit.getWorld("duel"), 1885.5, 98, 2072.5, 45, 0) ,new Location(Bukkit.getWorld("duel"), 1852.5, 98, 2101.5, -135, 0) ,new Location(Bukkit.getWorld("duel"), 1843, 115, 2062) , new Location(Bukkit.getWorld("duel"), 1896, 94, 2115), true);

                new DKit("Arena", "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", Material.DIAMOND_HELMET, false);
                new DKit("BuildUHC", "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c3EAfgADdXEAfgAGAAAAA3EAfgAIcQB+AAlxAH4AMnVxAH4ABgAAAANxAH4ADHEAfgBdcQB+AEU=", "rO0ABXcEAAAABHNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFwdAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmplY3Q7WwAGdmFsdWVzcQB+AAR4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAAAAN0AAI9PXQABHR5cGV0AARtZXRhdXEAfgAGAAAAA3QAHm9yZy5idWtraXQuaW52ZW50b3J5Lkl0ZW1TdGFja3QADURJQU1PTkRfQk9PVFNzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAh0AAltZXRhLXR5cGV0AAhlbmNoYW50c3QAC1VuYnJlYWthYmxldXEAfgAGAAAABHQACEl0ZW1NZXRhdAAKVU5TUEVDSUZJQ3NyADdjb20uZ29vZ2xlLmNvbW1vbi5jb2xsZWN0LkltbXV0YWJsZUJpTWFwJFNlcmlhbGl6ZWRGb3JtAAAAAAAAAAACAAB4cQB+AAN1cQB+AAYAAAABdAAYUFJPVEVDVElPTl9FTlZJUk9OTUVOVEFMdXEAfgAGAAAAAXNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAACc3IAEWphdmEubGFuZy5Cb29sZWFuzSBygNWc+u4CAAFaAAV2YWx1ZXhwAXNxAH4AAHNxAH4AA3VxAH4ABgAAAANxAH4ACHEAfgAJcQB+AAp1cQB+AAYAAAADcQB+AAx0ABBESUFNT05EX0xFR0dJTkdTc3EAfgAAc3EAfgADdXEAfgAGAAAAA3EAfgAIcQB+ABFxAH4AEnVxAH4ABgAAAANxAH4AFXEAfgAWc3EAfgAXdXEAfgAGAAAAAXQAFVBST1RFQ1RJT05fUFJPSkVDVElMRXVxAH4ABgAAAAFxAH4AHnNxAH4AAHNxAH4AA3VxAH4ABgAAAANxAH4ACHEAfgAJcQB+AAp1cQB+AAYAAAADcQB+AAx0ABJESUFNT05EX0NIRVNUUExBVEVzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAhxAH4AEXEAfgASdXEAfgAGAAAAA3EAfgAVcQB+ABZzcQB+ABd1cQB+AAYAAAABcQB+ABp1cQB+AAYAAAABcQB+AB5zcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAhxAH4ACXEAfgAKdXEAfgAGAAAAA3EAfgAMdAAORElBTU9ORF9IRUxNRVRzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAhxAH4AEXEAfgAScQB+ABN1cQB+AAYAAAAEcQB+ABVxAH4AFnNxAH4AF3VxAH4ABgAAAAFxAH4ALHVxAH4ABgAAAAFxAH4AHnEAfgAg", Material.LAVA_BUCKET, false);
                new DKit("FinalUHC", "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", Material.GOLDEN_APPLE, true);
                new DKit("SpeedFinalUHC", "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", Material.SUGAR, true);
                new DKit("Archer", "rO0ABXcEAAAAJHNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFwdAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmplY3Q7WwAGdmFsdWVzcQB+AAR4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAAAAN0AAI9PXQABHR5cGV0AARtZXRhdXEAfgAGAAAAA3QAHm9yZy5idWtraXQuaW52ZW50b3J5Lkl0ZW1TdGFja3QAA0JPV3NxAH4AAHNxAH4AA3VxAH4ABgAAAANxAH4ACHQACW1ldGEtdHlwZXQACGVuY2hhbnRzdXEAfgAGAAAAA3QACEl0ZW1NZXRhdAAKVU5TUEVDSUZJQ3NxAH4AA3VxAH4ABgAAAAJ0AAxBUlJPV19EQU1BR0V0AA5BUlJPV19JTkZJTklURXVxAH4ABgAAAAJzcgARamF2YS5sYW5nLkludGVnZXIS4qCk94GHOAIAAUkABXZhbHVleHIAEGphdmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAAAnNxAH4AGwAAAAFzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAhxAH4ACXQABmFtb3VudHVxAH4ABgAAAANxAH4ADHQAC0NPT0tFRF9CRUVGc3EAfgAbAAAAQHBwcHBwcHNxAH4AAHNxAH4AA3VxAH4ABgAAAAJxAH4ACHEAfgAJdXEAfgAGAAAAAnEAfgAMdAAFQVJST1dwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHA=", "rO0ABXcEAAAABHNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFwdAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmplY3Q7WwAGdmFsdWVzcQB+AAR4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAAAAN0AAI9PXQABHR5cGV0AARtZXRhdXEAfgAGAAAAA3QAHm9yZy5idWtraXQuaW52ZW50b3J5Lkl0ZW1TdGFja3QADUxFQVRIRVJfQk9PVFNzcQB+AABzcQB+AAN1cQB+AAYAAAADcQB+AAh0AAltZXRhLXR5cGV0AAhlbmNoYW50c3VxAH4ABgAAAAN0AAhJdGVtTWV0YXQADUxFQVRIRVJfQVJNT1JzcgA3Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVCaU1hcCRTZXJpYWxpemVkRm9ybQAAAAAAAAAAAgAAeHEAfgADdXEAfgAGAAAAAXQAFVBST1RFQ1RJT05fUFJPSkVDVElMRXVxAH4ABgAAAAFzcgARamF2YS5sYW5nLkludGVnZXIS4qCk94GHOAIAAUkABXZhbHVleHIAEGphdmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAABHNxAH4AAHNxAH4AA3VxAH4ABgAAAANxAH4ACHEAfgAJcQB+AAp1cQB+AAYAAAADcQB+AAx0ABBMRUFUSEVSX0xFR0dJTkdTc3EAfgAAc3EAfgADdXEAfgAGAAAAA3EAfgAIcQB+ABFxAH4AEnVxAH4ABgAAAANxAH4AFHEAfgAVc3EAfgAWdXEAfgAGAAAAAXEAfgAZdXEAfgAGAAAAAXEAfgAdc3EAfgAAc3EAfgADdXEAfgAGAAAAA3EAfgAIcQB+AAlxAH4ACnVxAH4ABgAAAANxAH4ADHQAEkxFQVRIRVJfQ0hFU1RQTEFURXNxAH4AAHNxAH4AA3VxAH4ABgAAAANxAH4ACHEAfgARcQB+ABJ1cQB+AAYAAAADcQB+ABRxAH4AFXNxAH4AFnVxAH4ABgAAAAFxAH4AGXVxAH4ABgAAAAFxAH4AHXNxAH4AAHNxAH4AA3VxAH4ABgAAAANxAH4ACHEAfgAJcQB+AAp1cQB+AAYAAAADcQB+AAx0AA5MRUFUSEVSX0hFTE1FVHNxAH4AAHNxAH4AA3VxAH4ABgAAAANxAH4ACHEAfgARcQB+ABJ1cQB+AAYAAAADcQB+ABRxAH4AFXNxAH4AFnVxAH4ABgAAAAFxAH4AGXVxAH4ABgAAAAFxAH4AHQ==", Material.BOW, true);
                for (DKit kit : kits){
                    queueranked.put(kit, new ArrayList<>());
                }
            }
        }.runTaskLater(Main.getInstance(), 20);

        World world = new WorldCreator("duel").createWorld();
        world.setTime(6000);
        world.setDifficulty(Difficulty.NORMAL);
        world.getWorldBorder().setCenter(0, 0);
        world.getWorldBorder().setDamageBuffer(1);
        world.getWorldBorder().setWarningTime(10);
        world.getWorldBorder().setWarningDistance(0);
        world.getWorldBorder().setDamageAmount(0.2);
        world.setGameRuleValue("naturalRegeneration", "false");
        world.setGameRuleValue("doFireTick", "false");
        world.setGameRuleValue("doDaylightCycle", "false");
    }

    public List<DMap> getMaps() {
        return maps;
    }

    public List<DKit> getKits() {
        return kits;
    }

    public List<DMap> getMapsUnUsed(boolean big) {
        List<DMap> value = new ArrayList<>();
        for (DMap map : getMaps()) {
            if (!map.isUsed()) {
                if (big && map.isBig())
                    value.add(map);
                else
                if (!big && !map.isBig())
                    value.add(map);
            }
        }
        return value;
    }


    public void joinQueue(boolean ranked, DKit kit, Player player){
        player.getInventory().clear();
        player.getInventory().setItem(8, new ItemCreator(Material.REDSTONE).setName(ChatColor.GOLD +"Leave Queue").getItem());
        LPlayer lp = Main.getInstance().getLPlayerByUniqueId(player.getUniqueId());
        lp.setWaiting(kit);
        queueranked.get(kit).add(lp);
        if (queueranked.get(kit).size() > 1){
            if (getMapsUnUsed(kit.isBigmap()).isEmpty()){
                queueranked.get(kit).get(0).getPlayer().sendMessage(Message.PREFIX.getMessage() + ChatColor.RED +  "No map available, try again later.");
                Main.getInstance().getDuel().leaveQueue(queueranked.get(kit).get(0).getPlayer());
                queueranked.get(kit).get(0).getPlayer().sendMessage(Message.PREFIX.getMessage() + ChatColor.RED +  "No map available, try again later.");
                Main.getInstance().getDuel().leaveQueue(queueranked.get(kit).get(0).getPlayer());
                return;
            }
            new DFight(Collections.singletonList(queueranked.get(kit).get(0)), Collections.singletonList(queueranked.get(kit).get(1)), kit, getMapsUnUsed(kit.isBigmap()).get(new Random().nextInt(getMapsUnUsed(kit.isBigmap()).size())), ranked);
            queueranked.get(kit).remove(0);
            queueranked.get(kit).remove(0);
        }
    }

    public void leaveQueue(Player player){
        LPlayer lp = Main.getInstance().getLPlayerByUniqueId(player.getUniqueId());
        if (lp.getWaiting() != null) {
            queueranked.get(lp.getWaiting()).remove(lp);
            lp.setWaiting(null);
        }
        if (Bukkit.getPlayer(player.getUniqueId()) != null) {
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.setMaxHealth(20);
            player.setHealth(20);
            player.setFoodLevel(20);
            player.setWalkSpeed(0.2f);
            player.getInventory().setHeldItemSlot(4);
            player.setGameMode(GameMode.ADVENTURE);
            player.getActivePotionEffects().clear();
            player.getInventory().setItem(0, new ItemCreator(Material.SKULL_ITEM).setOwner(player.getName()).setDurability(3).setName(ChatColor.GOLD + "Profile").getItem());
            player.getInventory().setItem(1, new ItemCreator(lp.isInvisible() ? Material.EYE_OF_ENDER : Material.ENDER_PEARL).setName(ChatColor.GOLD + "Players " + (lp.isInvisible() ? "Disabled" : "Enabled")).getItem());
            player.getInventory().setItem(2, new ItemCreator(Material.DIAMOND).setName(ChatColor.GOLD +"Donator").getItem());
            player.getInventory().setItem(4, new ItemCreator(Material.COMPASS).setName(ChatColor.GOLD + "Navigation").getItem());
            player.getInventory().setItem(6, new ItemCreator(Material.FEATHER).setName(ChatColor.GOLD + "Parkour").getItem());
            player.getInventory().setItem(7, new ItemCreator(Material.DIAMOND_HELMET).setName(ChatColor.GOLD + "Arena FFA").getItem());
            player.getInventory().setItem(8, new ItemCreator(Material.IRON_SWORD).setName(ChatColor.GOLD + "Duel").getItem());
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }

            player.teleport(new Location(Bukkit.getWorld("world"), 209.5, 101.5, 269.5, 0, 0));
            lp.loadCosmetic();
        }
    }

}