package fr.cobaldhub.utils.cosmetics;

import fr.cobaldhub.utils.cosmetics.mounts.*;
import fr.cobaldhub.utils.cosmetics.pets.*;
import fr.spigot.cobaldapi.utils.ItemCreator;
import org.bukkit.Material;

public enum ECosmetic {

    SPOOKY(new Spooky(), CType.MOUNTS, new ItemCreator(Material.SKULL_ITEM), false),
    BAT(new Bat(), CType.MOUNTS, new ItemCreator(Material.getMaterial(383)).setDurability(65), false),
    CHICKEN(new Chicken(), CType.MOUNTS, new ItemCreator(Material.getMaterial(383)).setDurability(93), false),
    DISCOSHEEP(new DiscoSheep(), CType.MOUNTS, new ItemCreator(Material.getMaterial(383)).setDurability(91), false),
    OCELOT(new Ocelot(), CType.MOUNTS, new ItemCreator(Material.getMaterial(383)).setDurability(98), false),
    SPIDER(new Spider(), CType.MOUNTS, new ItemCreator(Material.getMaterial(383)).setDurability(52), false),
    WOLF(new Wolf(), CType.MOUNTS, new ItemCreator(Material.getMaterial(383)).setDurability(95), false),
    GRIMREAPER(new GrimReaper(), CType.PETS, new ItemCreator(Material.IRON_HOE), false),
    COBALD(new Cobald(), CType.PETS, new ItemCreator(Material.SKULL_ITEM).setDurability(3).setOwner("PaB4tisteSVP"), false),
    CERBERUS(new Cerberus(), CType.PETS, new ItemCreator(Material.SKULL_ITEM).setDurability(3), true),
    EARTH(new Earth(), CType.PETS, new ItemCreator(Material.SKULL_ITEM).setDurability(3).setOwner("Seska_Rotan"), false),
    FLAMESPILLARS(new FlamesPillars(), CType.PETS, new ItemCreator(Material.FLINT_AND_STEEL), false),
    MARIO(new Mario(), CType.PETS, new ItemCreator(Material.SKULL_ITEM).setDurability(3).setOwner("cebolapop"), false),
    TV(new Tv(), CType.PETS, new ItemCreator(Material.SKULL_ITEM).setDurability(3).setOwner("Metroidling"), false),
    PIKACHU(new Pikachu(), CType.PETS, new ItemCreator(Material.SKULL_ITEM).setDurability(3).setOwner("Pikachubutler"), false);
    //HORSE(new Horse(), CType.PETS, new ItemCreator(Material.SADDLE), false);

    private Cosmetic cosmetic;
    public enum CType {MOUNTS, PETS}
    private CType type;
    private ItemCreator itemCreator;
    private boolean owner;
    ECosmetic(Cosmetic cosmetic, CType type, ItemCreator itemCreator, boolean owner){
        this.cosmetic = cosmetic;
        this.type = type;
        this.itemCreator = itemCreator;
        this.owner = owner;
    }

    public Cosmetic getCosmetic(){
        return cosmetic;
    }

    public CType getType() {
        return type;
    }

    public ItemCreator getItemCreator() {
        return itemCreator;
    }

    public boolean isOwner() {
        return owner;
    }
}
