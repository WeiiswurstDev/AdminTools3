package dev.wwst.admintools3.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUIBuilder {

    private Inventory inv;
    private MessageTranslator msg;

    public GUIBuilder(String invName, int rows) {
        if(invName.length() > 32) invName = invName.substring(0,32);
        inv = Bukkit.createInventory(null, rows*9, invName);
        msg = MessageTranslator.getInstance();
    }

    // 0 1 2 3 4 5 6 7 8

    public GUIBuilder fill(ItemStack item) {
        for(int slot = 0; slot < inv.getSize(); slot++) {
            inv.setItem(slot,item);
        }
        return this;
    }

    public GUIBuilder fillRow(ItemStack item, int row) {
        for(int slot = row*9; slot < (row+1)*9; slot++) {
            inv.setItem(slot,item);
        }
        return this;
    }

    public GUIBuilder setItem(int slot, ItemStack item) {
        inv.setItem(slot, item);
        return this;
    }

    public Inventory build() {
        return inv;
    }
}