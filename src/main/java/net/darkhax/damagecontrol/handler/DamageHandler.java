package net.darkhax.damagecontrol.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DamageHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onWorldLoad (WorldEvent.Load event) {
    	if (ConfigurationHandler.healthAllowed) {
	        for (final ResourceLocation entityId : EntityList.ENTITY_EGGS.keySet()) {
	
	            final Entity entity = EntityList.createEntityByIDFromName(entityId, event.getWorld());
	
	            if (entity instanceof EntityLivingBase) {
	
	                ConfigurationHandler.getMaxHealth(entityId, (EntityLivingBase) entity);
	            }
	        }
	    }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityJoinWorld (EntityJoinWorldEvent event) {
    	if (ConfigurationHandler.healthAllowed) {
	        if (event.getEntity() instanceof EntityLivingBase) {
	
	            final EntityLivingBase entity = (EntityLivingBase) event.getEntity();
	            final float modifiedMaxHealth = ConfigurationHandler.getMaxHealth(entity) * ConfigurationHandler.healthModifier;
	            
	            if (modifiedMaxHealth >= 0.0f) {
	            	final float currentMaxHealth = (float) entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
	            	
		            if (currentMaxHealth != modifiedMaxHealth) {
		                entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(modifiedMaxHealth);
		            }
		
		            if (entity.getHealth() == currentMaxHealth) {
		                entity.setHealth(modifiedMaxHealth);
		            }
	            }
	        }
	    }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityHurt (LivingHurtEvent event) {

        event.setAmount(event.getAmount() * ConfigurationHandler.getDamageModifier(event.getSource()));
    }
}