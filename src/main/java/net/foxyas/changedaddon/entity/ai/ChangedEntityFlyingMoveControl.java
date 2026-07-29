package net.foxyas.changedaddon.entity.ai;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class ChangedEntityFlyingMoveControl extends MoveControl {
    private final int maxTurn;
    protected final ChangedEntity changedEntity;
    private final boolean hoversInPlace;

    public ChangedEntityFlyingMoveControl(ChangedEntity pMob, int pMaxTurn, boolean pHoversInPlace) {
        super(pMob);
        this.changedEntity = pMob;
        this.maxTurn = pMaxTurn;
        this.hoversInPlace = pHoversInPlace;
    }

    public void tick() {
        if (this.operation == MoveControl.Operation.MOVE_TO) {
            this.operation = MoveControl.Operation.WAIT;
            this.changedEntity.setNoGravity(true);
            double dX = this.wantedX - this.changedEntity.getX();
            double dY = this.wantedY - this.changedEntity.getY();
            double dZ = this.wantedZ - this.changedEntity.getZ();
            double dDistance = dX * dX + dY * dY + dZ * dZ;
            if (dDistance < (double) 2.5000003E-7F) {
                this.changedEntity.setYya(0.0F);
                this.changedEntity.setZza(0.0F);
                return;
            }

            float f = (float) (Mth.atan2(dZ, dX) * (double) (180F / (float) Math.PI)) - 90.0F;
            this.changedEntity.setYRot(this.rotlerp(this.changedEntity.getYRot(), f, 90.0F));
            float f1;
            if (this.changedEntity.onGround()) {
                f1 = (float) (this.speedModifier * this.changedEntity.getAttributeValue(Attributes.MOVEMENT_SPEED));
            } else {
                f1 = (float) (this.speedModifier * changedEntity.getFlyingSpeed() * 4); // multiplier here might need to be tweaked to get speed similar to tf player
            }

            float horizontalMomento = f1;

            this.changedEntity.setSpeed(horizontalMomento);

            double xzSpeedSqrt = Math.sqrt(dX * dX + dZ * dZ);
            if (Math.abs(dY) > (double) 1.0E-5F || Math.abs(xzSpeedSqrt) > (double) 1.0E-5F) {
                float f2 = (float) (-(Mth.atan2(dY, xzSpeedSqrt) * (double) (180F / (float) Math.PI)));
                this.changedEntity.setXRot(this.rotlerp(this.changedEntity.getXRot(), f2, (float) this.maxTurn));
                this.changedEntity.setYya(dY > 0.0D ? f1 : -f1);
            }
        } else {
            if (!this.hoversInPlace) {
                this.changedEntity.setNoGravity(false);
            }

            this.changedEntity.setYya(0.0F);
            this.changedEntity.setZza(0.0F);
        }

    }
}