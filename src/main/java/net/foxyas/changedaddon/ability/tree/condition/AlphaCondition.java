package net.foxyas.changedaddon.ability.tree.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.changedaddon.ability.tree.FloatOperation;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.tree.condition.AbstractCondition;

import java.util.Optional;

public class AlphaCondition extends AbstractCondition {

    public static final Codec<AlphaCondition> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.optionalFieldOf("sizeReference").forGetter(AlphaCondition::getSizeReference),
                    FloatOperation.CODEC.optionalFieldOf("operation").forGetter(AlphaCondition::getOperation))
                    .apply(instance, AlphaCondition::new)
    );

    protected final Optional<Float> sizeReference;
    protected final Optional<FloatOperation> operation;

    public AlphaCondition(Optional<Float> sizeReference, Optional<FloatOperation> operation) {
        super();
        this.sizeReference = sizeReference;
        this.operation = operation;
    }

    public Optional<Float> getSizeReference() {
        return sizeReference;
    }

    public Optional<FloatOperation> getOperation() {
        return operation;
    }

    public float getSizeReferenceSafe() {
        return sizeReference.orElse(0.75f);
    }

    public FloatOperation getOperationSafe() {
        return operation.orElse(FloatOperation.GREATER_THAN_EQUAL_TO);
    }

    @Override
    public Codec<? extends AbstractCondition> getCodec() {
        return CODEC;
    }

    @Override
    public boolean test(IAbstractChangedEntity iAbstractChangedEntity) {
        if (getSizeReference().isPresent()) {
            return iAbstractChangedEntity.getChangedEntity() instanceof IAlphaAbleEntity alphaAbleEntity
                    && alphaAbleEntity.isAlpha() && this.getOperationSafe().test(alphaAbleEntity.alphaAdditionalScale(), getSizeReferenceSafe());
        }
        return iAbstractChangedEntity.getChangedEntity() instanceof IAlphaAbleEntity alphaAbleEntity && alphaAbleEntity.isAlpha();
    }
}
