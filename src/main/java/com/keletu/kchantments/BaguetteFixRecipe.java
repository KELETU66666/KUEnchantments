package com.keletu.kchantments;

import com.keletu.kchantments.item.ItemBaguette;
import static com.keletu.kchantments.item.ItemBaguette.*;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;

public class BaguetteFixRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	@Override
	public boolean isDynamic() {
		return true;
	}

	@Override
	public boolean matches(@Nonnull InventoryCrafting var1, @Nonnull World var2) {
		boolean foundBread = false;
		boolean foundItem = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() == Items.BREAD && !foundBread)
					foundBread = true;
				else if(!foundItem) {
					if(stack.getItem() instanceof ItemBaguette)
						foundItem = true;
					else return false;
				}
			}
		}

		return foundBread && foundItem;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
		ItemStack item = ItemStack.EMPTY;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ItemBaguette && item.isEmpty())
					item = stack;
			}
		}

		ItemStack copy = item.copy();
		ItemNBTHelper.setInt(copy, NBTTAG_DURABILITY, MaxDurability);
		ItemNBTHelper.setInt(copy, NBTTAG_LEFT, MaxUse);
		return copy;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width > 1 || height > 1;
	}

	@Nonnull
	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}
}