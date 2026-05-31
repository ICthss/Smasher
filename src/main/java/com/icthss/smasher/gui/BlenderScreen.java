package com.icthss.smasher.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BlenderScreen extends AbstractContainerScreen<BlenderMenu> {
    // 指向你的 GUI 贴图路径：src/main/resources/assets/smasher/textures/gui/blender_gui.png
    private static final ResourceLocation TEXTURE = 
            ResourceLocation.fromNamespaceAndPath("smasher", "textures/gui/blender_gui.png");

    public BlenderScreen(BlenderMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2; // 标题居中
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY); // 渲染物品浮动提示
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        
        // 1. 绘制基础背景大图
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        // 假设贴图右侧（X=176, Y=14）有一个 24x19 大小的朝右箭头
        int l = this.menu.getScaledProgress();
        guiGraphics.blit(TEXTURE, x + 76, y + 25, 176, 13, l, 19);
    }
}
