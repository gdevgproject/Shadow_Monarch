package dev.umbra.core.impl.content;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.umbra.UmbraMod;
import dev.umbra.core.contract.content.EnemyDefinition;
import dev.umbra.core.contract.content.ReferenceCard;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public final class ContentLoader {
    private final ContentRegistryImpl registry;

    public ContentLoader(ContentRegistryImpl registry) {
        this.registry = registry;
    }

    public boolean loadEnemy(String filename, String jsonContent) {
        try {
            Map<String, Integer> lineMap = JsonLocationTracker.trackLines(jsonContent);
            JsonElement jsonElement = JsonParser.parseString(jsonContent);

            DataResult<EnemyDefinition> decodeResult = EnemyDefinition.CODEC.parse(JsonOps.INSTANCE, jsonElement);
            if (decodeResult.error().isPresent()) {
                String errorMsg = decodeResult.error().get().message();
                UmbraMod.LOGGER.error("Codec validation error in [{}]: {}", filename, errorMsg);
                return false;
            }

            EnemyDefinition enemy = decodeResult.result().get();

            List<ValidationError> errors = ContentValidator.validate(enemy, lineMap);
            if (!errors.isEmpty()) {
                UmbraMod.LOGGER.error("Validation failed for enemy definition [{}]:", filename);
                for (ValidationError err : errors) {
                    UmbraMod.LOGGER.error("  {}", err.toString());
                }
                return false;
            }

            registry.registerEnemy(enemy);
            return true;
        } catch (JsonSyntaxException e) {
            UmbraMod.LOGGER.error("JSON syntax error in [{}]: {}", filename, e.getMessage());
            return false;
        } catch (IOException e) {
            UmbraMod.LOGGER.error("I/O error reading [{}]: {}", filename, e.getMessage());
            return false;
        } catch (Exception e) {
            UmbraMod.LOGGER.error("Unexpected error loading enemy [{}]: {}", filename, e.getMessage(), e);
            return false;
        }
    }

    public boolean loadReferenceCard(String filename, String jsonContent) {
        try {
            Map<String, Integer> lineMap = JsonLocationTracker.trackLines(jsonContent);
            JsonElement jsonElement = JsonParser.parseString(jsonContent);

            DataResult<ReferenceCard> decodeResult = ReferenceCard.CODEC.parse(JsonOps.INSTANCE, jsonElement);
            if (decodeResult.error().isPresent()) {
                String errorMsg = decodeResult.error().get().message();
                UmbraMod.LOGGER.error("Codec validation error in [{}]: {}", filename, errorMsg);
                return false;
            }

            ReferenceCard card = decodeResult.result().get();

            List<ValidationError> errors = ContentValidator.validate(card, lineMap);
            if (!errors.isEmpty()) {
                UmbraMod.LOGGER.error("Validation failed for reference card [{}]:", filename);
                for (ValidationError err : errors) {
                    UmbraMod.LOGGER.error("  {}", err.toString());
                }
                return false;
            }

            registry.registerReferenceCard(card);
            return true;
        } catch (JsonSyntaxException e) {
            UmbraMod.LOGGER.error("JSON syntax error in [{}]: {}", filename, e.getMessage());
            return false;
        } catch (IOException e) {
            UmbraMod.LOGGER.error("I/O error reading [{}]: {}", filename, e.getMessage());
            return false;
        } catch (Exception e) {
            UmbraMod.LOGGER.error("Unexpected error loading reference card [{}]: {}", filename, e.getMessage(), e);
            return false;
        }
    }
}
