package net.chazza.credits.shop;

import com.hazebyte.base.Base;
import com.hazebyte.base.Button;
import com.hazebyte.base.Size;
import com.hazebyte.base.foundation.CloseButton;
import com.hazebyte.base.foundation.NextButton;
import com.hazebyte.base.foundation.PreviousButton;
import net.chazza.credits.Credits;
import net.chazza.credits.storage.PlayerData;
import net.chazza.credits.util.ActionHandler;
import net.chazza.credits.util.Common;
import net.chazza.credits.util.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShopInventory extends Base {

    public ShopInventory(Credits plugin, Player player) {
        super(plugin, Lang.GUI_TITLE.asString(Lang.PREFIX.asString(), Common.getPlayer(player.getUniqueId()).getCredits(), plugin.getShopManager().getShopItems().size()),
                Size.from(plugin.getConfig().getInt("settings.slots", 27)));

        PlayerData playerData = Common.getPlayer(player.getUniqueId());

        int guiSlots = plugin.getConfig().getInt("settings.slots", 27);
        int shopItemCount = 0;
        for (ShopItem shopItem : plugin.getShopManager().getShopItems()) {
            if (shopItem.getLimit() != -1 && (playerData.getPurchases().containsKey(shopItem.getId()) && (playerData.getPurchases().get(shopItem.getId()) <= shopItem.getLimit()))) {
                continue;
            }

            shopItemCount++;
            Button shopBtn = new Button(shopItem.getItem());
            shopBtn.setAction(buttonClickEvent -> {
                if (playerData.getCredits() < shopItem.getPrice()) {
                    Lang.CANNOT_AFFORD.send(player, Lang.HEADER.asString(), Lang.PREFIX.asString(), shopItem.getPrice() - playerData.getCredits(), Lang.FOOTER.asString());
                } else {
                    ActionHandler.executeActions(player, shopItem.getActions());
                    int newAmount = playerData.getCredits() - shopItem.getPrice();
                    Lang.PURCHASED.send(player, Lang.HEADER.asString(), Lang.PREFIX.asString(), shopItem.getPrice(), newAmount, Lang.FOOTER.asString());
                    int purchaseTotal = playerData.getPurchases().getOrDefault(shopItem.getId(), 0);
                    playerData.getPurchases().put(shopItem.getId(), purchaseTotal + 1);

                    playerData.setCredits(newAmount);
                }
                close(player);
            });
            this.attach(shopBtn);
            this.addIcon(shopBtn);
        }


        int pages = (int) Math.ceil((double) shopItemCount / guiSlots - 9);
        List<Integer> navPages = new ArrayList<>();

        for (int i = 0; i < pages; i++) {
            navPages.add(i);
        }

        this.setIcon(navPages.stream().mapToInt(i -> i).toArray(), guiSlots - 6, new PreviousButton());
        this.setIcon(guiSlots - 5, new CloseButton(new ItemStack(Material.IRON_DOOR)));
        this.setIcon(navPages.stream().mapToInt(i -> i).toArray(), guiSlots - 4, new NextButton());
    }
}
