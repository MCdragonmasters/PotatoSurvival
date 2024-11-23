package com.mcdragonmasters.potatosurvival.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Objects;

import static com.mcdragonmasters.potatosurvival.PotatoSurvival.instance;

public class Sit implements Listener {
    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        Block block = e.getClickedBlock();
        BlockData blockData = Objects.requireNonNull(block).getBlockData();
        if (blockData instanceof Stairs stairs) {
            Player p = e.getPlayer();
            if (stairs.isWaterlogged()) return;
            if (p.isSneaking()) return;
            if (stairs.getHalf() == Bisected.Half.TOP) return;
            if (p.isInsideVehicle()) return;
            e.setCancelled(true);

            Location loc = block.getLocation();

            double adderZ = 0.5;
            double adderY = 0.7;
            double adderX = 0.5;

            loc.setX(loc.getX() + adderX);
            loc.setY(loc.getY() + adderY);
            loc.setZ(loc.getZ() + adderZ);

            BlockFace facing = ((Directional) blockData).getFacing();
            switch (facing) {
                case SOUTH:
                    loc.setYaw(180);
                    break;
                case WEST:
                    loc.setYaw(270);
                    break;
                case EAST:
                    loc.setYaw(90);
                    break;
                case NORTH:
                    loc.setYaw(0);
                    break;
            }
            Stairs.Shape shape = stairs.getShape();
            if (shape == Stairs.Shape.INNER_RIGHT || shape == Stairs.Shape.OUTER_RIGHT) {
                loc.setYaw(loc.getYaw() + 45);
            } else if (shape == Stairs.Shape.INNER_LEFT || shape == Stairs.Shape.OUTER_LEFT) {
                loc.setYaw(loc.getYaw() - 45);
            }
            Entity entity = p.getWorld().spawn(loc, Objects.requireNonNull(EntityType.ARMOR_STAND.getEntityClass()), (chair -> {
                ArmorStand stand = (ArmorStand) chair;
                stand.setPersistent(false);
                stand.setGravity(false);
                stand.setSmall(true);
                stand.setMarker(true);
                stand.setInvisible(true);
                stand.setInvulnerable(true);
                stand.setBasePlate(false);
                stand.setMetadata("chair", new FixedMetadataValue(instance, true));
            }));
            entity.addPassenger(p);

        }
    }
    @EventHandler
    public void onDismount(EntityDismountEvent e) {
        if (e.getDismounted().hasMetadata("chair")) {
            Bukkit.getScheduler().runTaskLater(instance, () -> e.getDismounted().remove(), 1L);
        }
    }
}
