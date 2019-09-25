package io.felux.client.credits.shop;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ShopItem {

    private String id;
    private int price;
    private int limit;
    private List<String> actions;
    private ItemStack item;

    public ShopItem(String id, int price, int limit, List<String> actions, ItemStack item) {
        this.id = id;
        this.price = price;
        this.limit = limit;
        this.actions = actions;
        this.item = item;
    }

    public String getId() {
        return id;
    }

    public int getLimit() {
        return limit;
    }

    public int getPrice() {
        return price;
    }

    public List<String> getActions() {
        return actions;
    }

    public ItemStack getItem() {
        return item;
    }
}
