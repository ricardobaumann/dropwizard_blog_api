package com.github.ricardobaumann.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.joda.time.DateTime;

public class AccessTokenDAO {

    private static Map<UUID, AccessToken> accessTokenTable = new HashMap<>();

    public Optional<AccessToken> findAccessTokenById(final UUID accessTokenId) {
        AccessToken accessToken = accessTokenTable.get(accessTokenId);
        if (accessToken == null) {
            return Optional.empty();
        }
        return Optional.of(accessToken);
    }

    public AccessToken generateNewAccessToken(final User user, final DateTime dateTime) {
        AccessToken accessToken = new AccessToken(UUID.randomUUID(), user.getId(), dateTime);
        accessTokenTable.put(accessToken.getAccessTokenId(), accessToken);
        return accessToken;
    }

    public void setLastAccessTime(final UUID accessTokenUUID, final DateTime dateTime) {
        AccessToken accessToken = accessTokenTable.get(accessTokenUUID);
        AccessToken updatedAccessToken = accessToken.withLastAccessUTC(dateTime);
        accessTokenTable.put(accessTokenUUID, updatedAccessToken);
    }

}
