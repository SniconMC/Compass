package rip.snicon.utils;

import net.minestom.server.entity.EntityType;

public class EntityUtils {


    public static EntityType getEntityTypeFromNamespace(String namespaceId) {
        // Use EntityType.fromNamespaceId to get the corresponding EntityType enum
        EntityType entityType = EntityType.fromNamespaceId(namespaceId);

        if (entityType == null) {
            return EntityType.CAT;
        }
        return entityType;
    }
}
