package com.icthss.smasher.gui;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public class ModMenuTypes {

        public static final DeferredRegister<MenuType<?>> MENUS = 
            DeferredRegister.create(Registries.MENU, "smasher"); 
            
        public static final Supplier<MenuType<SmasherMenu>> SMASHER_MENU = MENUS.register("smasher_menu",
                () -> IMenuTypeExtension.create(SmasherMenu::new));

}
