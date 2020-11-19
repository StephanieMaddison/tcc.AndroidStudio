/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.api.internal.artifacts;

import org.gradle.api.internal.artifacts.ivyservice.ArtifactCacheMetadata;
import org.gradle.api.internal.artifacts.ivyservice.CacheLockingManager;
import org.gradle.api.internal.artifacts.ivyservice.DefaultCacheLockingManager;
import org.gradle.api.internal.artifacts.transform.DefaultTransformedFileCache;
import org.gradle.api.internal.artifacts.transform.TransformedFileCache;
import org.gradle.api.internal.changedetection.state.FileSystemSnapshotter;
import org.gradle.api.internal.changedetection.state.InMemoryCacheDecoratorFactory;
import org.gradle.cache.CacheRepository;
import org.gradle.internal.event.ListenerManager;
import org.gradle.internal.service.ServiceRegistration;
import org.gradle.internal.service.scopes.AbstractPluginServiceRegistry;

public class DependencyServices extends AbstractPluginServiceRegistry {
    public void registerGlobalServices(ServiceRegistration registration) {
        registration.addProvider(new DependencyManagementGlobalScopeServices());
    }

    @Override
    public void registerGradleUserHomeServices(ServiceRegistration registration) {
        registration.addProvider(new DependencyManagementGradleUserHomeScopeServices());
    }

    @Override
    public void registerBuildSessionServices(ServiceRegistration registration) {
        registration.addProvider(new DependencyManagementBuildSessionServices());
    }

    @Override
    public void registerBuildServices(ServiceRegistration registration) {
        registration.addProvider(new DependencyManagementBuildScopeServices());
    }

    @Override
    public void registerBuildTreeServices(ServiceRegistration registration) {
        registration.addProvider(new DependencyManagementBuildTreeScopeServices());
    }

    private static class DependencyManagementBuildSessionServices {
        CacheLockingManager createCacheLockingManager(CacheRepository cacheRepository, ArtifactCacheMetadata artifactCacheMetadata) {
            return new DefaultCacheLockingManager(cacheRepository, artifactCacheMetadata);
        }

        TransformedFileCache createTransformedFileCache(ArtifactCacheMetadata artifactCacheMetadata, CacheRepository cacheRepository, InMemoryCacheDecoratorFactory cacheDecoratorFactory, FileSystemSnapshotter fileSystemSnapshotter, ListenerManager listenerManager) {
            DefaultTransformedFileCache transformedFileCache = new DefaultTransformedFileCache(artifactCacheMetadata, cacheRepository, cacheDecoratorFactory, fileSystemSnapshotter);
            listenerManager.addListener(transformedFileCache);
            return transformedFileCache;
        }
    }
}
