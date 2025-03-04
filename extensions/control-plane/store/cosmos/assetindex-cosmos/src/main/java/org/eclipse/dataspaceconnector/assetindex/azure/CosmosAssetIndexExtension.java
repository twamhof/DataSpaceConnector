/*
 *  Copyright (c) 2020, 2021 Microsoft Corporation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Microsoft Corporation - initial API and implementation
 *
 */

package org.eclipse.dataspaceconnector.assetindex.azure;

import dev.failsafe.RetryPolicy;
import org.eclipse.dataspaceconnector.assetindex.azure.model.AssetDocument;
import org.eclipse.dataspaceconnector.azure.cosmos.CosmosClientProvider;
import org.eclipse.dataspaceconnector.azure.cosmos.CosmosDbApiImpl;
import org.eclipse.dataspaceconnector.runtime.metamodel.annotation.Extension;
import org.eclipse.dataspaceconnector.runtime.metamodel.annotation.Inject;
import org.eclipse.dataspaceconnector.runtime.metamodel.annotation.Provides;
import org.eclipse.dataspaceconnector.spi.asset.AssetIndex;
import org.eclipse.dataspaceconnector.spi.asset.DataAddressResolver;
import org.eclipse.dataspaceconnector.spi.security.Vault;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtension;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtensionContext;
import org.eclipse.dataspaceconnector.spi.system.health.HealthCheckService;

/**
 * Provides a persistent implementation of the {@link org.eclipse.dataspaceconnector.spi.asset.AssetIndex} using CosmosDB.
 */
@Provides({ AssetIndex.class, DataAddressResolver.class })
@Extension(value = CosmosAssetIndexExtension.NAME)
public class CosmosAssetIndexExtension implements ServiceExtension {

    public static final String NAME = "CosmosDB Asset Index";
    @Inject
    private Vault vault;
    @Inject
    private CosmosClientProvider clientProvider;

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        var configuration = new AssetIndexCosmosConfig(context);

        var client = clientProvider.createClient(vault, configuration);
        var cosmosDbApi = new CosmosDbApiImpl(configuration, client);
        var assetIndex = new CosmosAssetIndex(cosmosDbApi, configuration.getPartitionKey(), context.getTypeManager(), context.getService(RetryPolicy.class), context.getMonitor());
        context.registerService(AssetIndex.class, assetIndex);
        context.registerService(DataAddressResolver.class, assetIndex);

        context.getService(HealthCheckService.class).addReadinessProvider(() -> cosmosDbApi.get().forComponent(name()));

        context.getTypeManager().registerTypes(AssetDocument.class);
    }

}
