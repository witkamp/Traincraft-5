/*
 * This file ("TrainRenderer.java") is part of the Traincraft mod for Minecraft.
 * It is created by all people that are listed with @author below.
 * It is distributed under the Traincraft License (https://github.com/Traincraft/Traincraft/blob/master/LICENSE.md)
 * You can find the source code at https://github.com/Traincraft/Traincraft
 *
 * © 2011-2016
 */

package si.meansoft.traincraft.client.renderer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import si.meansoft.traincraft.api.TrainBase;
import si.meansoft.traincraft.client.models.TrainModel;

import javax.annotation.Nullable;

/**
 * @author canitzp
 */
public class TrainRenderer<T extends TrainBase> extends Render<T> implements IResourceManagerReloadListener{

    private TrainModel<T> model;
    private ResourceLocation loc;
    private boolean dirty = true;

    public TrainRenderer(RenderManager renderManager, TrainModel<T> model) {
        super(renderManager);
        this.model = model;
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        if(this.dirty){
            this.dirty = false;
            //entity.processModelChanges(this.model);
            return this.loc = this.model.getTexture(entity);
        }
        return this.loc;
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(entity.rotationYaw, 0, 1, 0);
        GlStateManager.rotate(entity.rotationPitch, 1, 0, 0);
        GlStateManager.translate(-model.getMaxWidth() / 2 + model.getXOffset(), -model.getYOffset() + model.getWheelHeight(), -model.getMaxDepth() / 2 + model.getZOffset());
        this.bindEntityTexture(entity);
        this.model.render();
        GlStateManager.popMatrix();
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.model.reInitParts();
        this.dirty = true;
    }

    public static final float radianF = (float) Math.PI / 180.0f;
    public static float[] rotatePoint(float[] f, float pitch, float yaw, float roll) {
        float cos;
        float sin;
        float[] xyz = new float[]{f[0],f[1],f[2]};

        if (pitch != 0.0F) {
            pitch *= radianF;
            cos = MathHelper.cos(pitch);
            sin = MathHelper.sin(pitch);

            xyz[0] = (f[1] * sin) + (f[0] * cos);
            xyz[1] = (f[1] * cos) - (f[0] * sin);
        }

        if (yaw != 0.0F) {
            yaw *= radianF;
            cos = MathHelper.cos(yaw);
            sin = MathHelper.sin(yaw);

            xyz[0] = (f[0] * cos) - (f[2] * sin);
            xyz[2] = (f[0] * sin) + (f[2] * cos);
        }

        if (roll != 0.0F) {
            roll *=  radianF;
            cos = MathHelper.cos(roll);
            sin = MathHelper.sin(roll);

            xyz[1] = (f[2] * cos) - (f[1] * sin);
            xyz[2] = (f[2] * sin) + (f[1] * cos);
        }

        return xyz;
    }
}
