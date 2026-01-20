package net.lordgono.umbrawolves.entity;

public enum WolfEquipmentSlot {
    HEAD("head"),      // Helmets, space helmet
    BODY("body"),      // Armor, sweater, space suit
    FEET("feet");      // Booties

    private final String name;

    WolfEquipmentSlot(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getNbtKey() {
        return "WolfEquipment_" + name;
    }
}
