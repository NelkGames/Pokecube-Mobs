package pokecube.mobs.client.smd.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import net.minecraft.util.ResourceLocation;

/**
 * Base model object, this contains the body, a list of the bones, and the
 * animations.
 */
public class Model
{
    public Body                       body;
    public HashMap<String, Animation> anims         = new HashMap<>();
    public Bone                       root;
    public ArrayList<Bone>            allBones;
    public Animation                  currentAnimation;
    public boolean                    hasAnimations = true;
    public boolean                    usesMaterials = true;

    public Model(Model model)
    {
        this.body = new Body(model.body, this);
        final Iterator<Map.Entry<String, Animation>> iterator = model.anims.entrySet().iterator();
        while (iterator.hasNext())
        {
            final Map.Entry<String, Animation> entry = iterator.next();
            this.anims.put(entry.getKey(), new Animation(entry.getValue(), this));
        }
        this.hasAnimations = model.hasAnimations;
        this.usesMaterials = model.usesMaterials;
        this.currentAnimation = this.anims.get("idle");
    }

    public Model(ResourceLocation resource) throws Exception
    {
        this.load(resource);
        this.reformBones();
        this.precalculateAnims();
    }

    public void animate()
    {
        this.resetVerts(this.body);
        if (this.body.currentAnim == null) this.setAnimation("idle");
        this.root.prepareTransform();
        for (final Bone b : this.allBones)
            b.applyTransform();
        this.applyVertChange(this.body);
    }

    private void applyVertChange(Body body)
    {
        if (body == null) return;
        for (final MutableVertex v : body.verts)
            v.apply();
    }

    public boolean hasAnimations()
    {
        return this.hasAnimations;
    }

    private void load(ResourceLocation resloc) throws Exception
    {
        try
        {
            final ResourceLocation modelPath = resloc;
            // Load the model.
            this.body = new Body(this, modelPath);

            final List<String> anims = Lists.newArrayList("idle", "walking", "flying", "sleeping", "swimming");
            final String resLoc = resloc.toString();
            // Check for valid animations, and load them in as well.
            for (final String s : anims)
            {
                final String anim = resLoc.endsWith("smd") ? resLoc.replace(".smd", "/" + s + ".smd")
                        : resLoc.replace(".SMD", "/" + s + ".smd");
                final ResourceLocation animation = new ResourceLocation(anim);
                try
                {
                    this.anims.put(s, new Animation(this, s, animation));
                    if (s.equalsIgnoreCase("idle")) this.currentAnimation = this.anims.get(s);
                }
                catch (final Exception e)
                {
                    // e.printStackTrace();
                }
            }
        }
        catch (final Exception e)
        {
            throw e;
        }
    }

    private void precalculateAnims()
    {
        for (final Animation anim : this.anims.values())
            anim.precalculateAnimation(this.body);
    }

    private void reformBones()
    {
        this.root.applyChildrenToRest();
        for (final Bone b : this.allBones)
            b.invertRestMatrix();
    }

    public void renderAll()
    {
        GL11.glShadeModel(GL11.GL_SMOOTH);
        this.body.render();
        GL11.glShadeModel(GL11.GL_FLAT);
    }

    private void resetVerts(Body body)
    {
        if (body == null) return;
        for (final MutableVertex v : body.verts)
            v.reset();
    }

    public void setAnimation(String name)
    {
        final Animation old = this.currentAnimation;
        if (this.anims.containsKey(name)) this.currentAnimation = this.anims.get(name);
        else this.currentAnimation = this.anims.get("idle");
        this.body.setAnimation(this.currentAnimation);
        if (old != this.currentAnimation)
        {
        }
    }

    void syncBones(Body body)
    {
        this.allBones = body.bones;
        if (!body.partOfGroup) this.root = body.root;
    }
}