package com.icthss.smasher.client;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import com.icthss.smasher.gui.ModMenuTypes;
import com.icthss.smasher.gui.SmasherScreen;
import com.icthss.smasher.gui.BlenderScreen;

public class ClientSetupBusSubscriber {

    public void registerScreens(RegisterMenuScreensEvent event) {
        
        // 绑定 Menu 和 Screen
        event.register(
                ModMenuTypes.SMASHER_MENU.get(), 
                SmasherScreen::new
        );
        event.register(
                ModMenuTypes.BLENDER_MENU.get(),
                BlenderScreen::new     
        );
        
    }
}
