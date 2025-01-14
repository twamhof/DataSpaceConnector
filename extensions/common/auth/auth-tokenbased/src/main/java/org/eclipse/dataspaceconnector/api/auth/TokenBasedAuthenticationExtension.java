/*
 *  Copyright (c) 2020 - 2022 Microsoft Corporation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Microsoft Corporation - initial API and implementation
 *       Mercedes-Benz Tech Innovation GmbH - add README.md; authentication key can be retrieved from vault
 *       Fraunhofer Institute for Software and Systems Engineering - update monitor info
 *
 */

package org.eclipse.dataspaceconnector.api.auth;

import org.eclipse.dataspaceconnector.runtime.metamodel.annotation.EdcSetting;
import org.eclipse.dataspaceconnector.runtime.metamodel.annotation.Extension;
import org.eclipse.dataspaceconnector.runtime.metamodel.annotation.Inject;
import org.eclipse.dataspaceconnector.runtime.metamodel.annotation.Provides;
import org.eclipse.dataspaceconnector.spi.security.Vault;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtension;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtensionContext;

import java.util.UUID;

/**
 * Extension that registers an AuthenticationService that uses API Keys
 */
@Provides(AuthenticationService.class)
@Extension(value = TokenBasedAuthenticationExtension.NAME)
public class TokenBasedAuthenticationExtension implements ServiceExtension {

    public static final String NAME = "Static token API Authentication";
    @EdcSetting
    private static final String AUTH_SETTING_APIKEY = "edc.api.auth.key";
    @EdcSetting
    private static final String AUTH_SETTING_APIKEY_ALIAS = "edc.api.auth.key.alias";
    @Inject
    private Vault vault;

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        String apiKey = null;

        var apiKeyAlias = context.getSetting(AUTH_SETTING_APIKEY_ALIAS, null);
        if (apiKeyAlias != null) {
            apiKey = vault.resolveSecret(apiKeyAlias);
        }

        if (apiKey == null) {
            apiKey = context.getSetting(AUTH_SETTING_APIKEY, UUID.randomUUID().toString());
        }

        context.registerService(AuthenticationService.class, new TokenBasedAuthenticationService(apiKey));
    }
}
