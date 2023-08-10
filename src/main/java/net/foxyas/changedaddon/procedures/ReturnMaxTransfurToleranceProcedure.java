package net.foxyas.changedaddon.procedures;
import net.ltxprogrammer.changed.Changed;

public class ReturnMaxTransfurToleranceProcedure {
	public static double execute() {
		float MaxTransfurTolerance = Changed.config.server.transfurTolerance.get().floatValue();
		return MaxTransfurTolerance;
	}
}
