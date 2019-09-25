package io.felux.credits.shop;

import java.util.HashSet;
import java.util.Set;

public class ShopManager {

    private Set<ShopItem> shopItems;

    public ShopManager() {
        this.shopItems = new HashSet<>();
    }

    public Set<ShopItem> getShopItems() {
        return shopItems;
    }

    public void addItem(ShopItem shopItem) {
        shopItems.add(shopItem);
    }

    public ShopItem getShop(String shopId) {
        for (ShopItem shopItem : shopItems) {
            if (shopItem.getId().equalsIgnoreCase(shopId)) {
                return shopItem;
            }
        }

        return null;
    }
}
