package pokecube.mobs.client.render;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;
import pokecube.core.interfaces.IPokemob;
import pokecube.core.interfaces.capabilities.CapabilityPokemob;
import thut.core.client.render.animation.ModelHolder;
import thut.core.client.render.model.IExtendedModelPart;
import thut.core.client.render.model.IModelRenderer;
import thut.core.client.render.texturing.IRetexturableModel;
import thut.core.client.render.wrappers.ModelWrapper;

public class ModelWrapperSpinda<T extends MobEntity> extends ModelWrapper<T>
{
    private static final ResourceLocation normalh  = new ResourceLocation("pokecube_mobs",
            "gen_3/entity/textures/spindaspotsh.png");
    private static final ResourceLocation normalhb = new ResourceLocation("pokecube_mobs",
            "gen_3/entity/textures/spindaheadbase.png");
    private static final ResourceLocation shinyh   = new ResourceLocation("pokecube_mobs",
            "gen_3/entity/textures/spindaspotshs.png");
    private static final ResourceLocation shinyhb  = new ResourceLocation("pokecube_mobs",
            "gen_3/entity/textures/spindaheadbases.png");
    private static final ResourceLocation normale  = new ResourceLocation("pokecube_mobs",
            "gen_3/entity/textures/spindaspotse.png");
    private static final ResourceLocation normaleb = new ResourceLocation("pokecube_mobs",
            "gen_3/entity/textures/spindaearsbase.png");
    private static final ResourceLocation shinye   = new ResourceLocation("pokecube_mobs",
            "gen_3/entity/textures/spindaspotses.png");
    private static final ResourceLocation shinyeb  = new ResourceLocation("pokecube_mobs",
            "gen_3/entity/textures/spindaearsbases.png");

    public ModelWrapperSpinda(ModelHolder model, IModelRenderer<?> renderer)
    {
        super(model, renderer);
    }

    /** Sets the models various rotation angles then renders the model. */
    @Override
    public void render(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
            float headPitch, float scale)
    {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        final IPokemob spinda = CapabilityPokemob.getPokemobFor(entityIn);
        for (final String partName : this.imodel.getParts().keySet())
        {
            final IExtendedModelPart part = this.imodel.getParts().get(partName);
            if (part == null) continue;
            try
            {
                if (part.getParent() == null)
                {
                    final Random rand = new Random(spinda.getRNGValue());
                    ((IRetexturableModel) part).setTexturer(null);

                    // Render the base layer of the head and ears
                    GlStateManager.pushMatrix();
                    Minecraft.getInstance().getTextureManager().bindTexture(spinda.isShiny()
                            ? ModelWrapperSpinda.shinyhb : ModelWrapperSpinda.normalhb);
                    part.renderOnly("Head");
                    GlStateManager.popMatrix();
                    GlStateManager.pushMatrix();
                    Minecraft.getInstance().getTextureManager().bindTexture(spinda.isShiny()
                            ? ModelWrapperSpinda.shinyeb : ModelWrapperSpinda.normaleb);
                    part.renderOnly("Left_ear");
                    GlStateManager.popMatrix();
                    GlStateManager.pushMatrix();
                    Minecraft.getInstance().getTextureManager().bindTexture(spinda.isShiny()
                            ? ModelWrapperSpinda.shinyeb : ModelWrapperSpinda.normaleb);
                    part.renderOnly("Right_ear");
                    GlStateManager.popMatrix();

                    // Render the 4 spots
                    for (int i = 0; i < 4; i++)
                    {
                        float dx = rand.nextFloat();
                        float dy = rand.nextFloat() / 2 + 0.5f;
                        GlStateManager.pushMatrix();
                        GL11.glMatrixMode(GL11.GL_TEXTURE);
                        GL11.glLoadIdentity();
                        GL11.glTranslatef(dx, dy, 0.0F);
                        GL11.glMatrixMode(GL11.GL_MODELVIEW);
                        Minecraft.getInstance().getTextureManager().bindTexture(spinda.isShiny()
                                ? ModelWrapperSpinda.shinyh : ModelWrapperSpinda.normalh);
                        part.renderOnly("Head");
                        GL11.glMatrixMode(GL11.GL_TEXTURE);
                        GL11.glLoadIdentity();
                        GL11.glMatrixMode(GL11.GL_MODELVIEW);
                        GlStateManager.popMatrix();
                        GlStateManager.pushMatrix();
                        GL11.glMatrixMode(GL11.GL_TEXTURE);
                        GL11.glLoadIdentity();
                        dx = rand.nextFloat();
                        dy = rand.nextFloat() / 2 + 0.5f;
                        GL11.glTranslatef(dx, dy, 0.0F);
                        GL11.glMatrixMode(GL11.GL_MODELVIEW);
                        Minecraft.getInstance().getTextureManager().bindTexture(spinda.isShiny()
                                ? ModelWrapperSpinda.shinye : ModelWrapperSpinda.normale);
                        part.renderOnly("Left_ear");
                        GL11.glMatrixMode(GL11.GL_TEXTURE);
                        GL11.glLoadIdentity();
                        GL11.glMatrixMode(GL11.GL_MODELVIEW);
                        GlStateManager.popMatrix();
                        GlStateManager.pushMatrix();
                        GL11.glMatrixMode(GL11.GL_TEXTURE);
                        GL11.glLoadIdentity();
                        dx = rand.nextFloat();
                        dy = rand.nextFloat() / 2 + 0.5f;
                        GL11.glTranslatef(dx, dy, 0.0F);
                        GL11.glMatrixMode(GL11.GL_MODELVIEW);
                        part.renderOnly("Right_ear");
                        GL11.glMatrixMode(GL11.GL_TEXTURE);
                        GL11.glLoadIdentity();
                        GL11.glMatrixMode(GL11.GL_MODELVIEW);
                        GlStateManager.popMatrix();
                    }
                    // Render the model normally.
                    if (this.renderer.getTexturer() != null && part instanceof IRetexturableModel)
                    {
                        this.renderer.getTexturer().bindObject(entityIn);
                        ((IRetexturableModel) part).setTexturer(this.renderer.getTexturer());
                    }
                    GlStateManager.pushMatrix();
                    part.renderAll();
                    GlStateManager.popMatrix();
                }
            }
            catch (final Exception e)
            {
                e.printStackTrace();
            }
        }
        GlStateManager.color4f(1, 1, 1, 1);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }
}
