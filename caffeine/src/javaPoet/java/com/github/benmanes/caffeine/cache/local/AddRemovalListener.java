/*
 * Copyright 2015 Ben Manes. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.benmanes.caffeine.cache.local;

import static com.github.benmanes.caffeine.cache.Specifications.REMOVAL_LISTENER;

import javax.lang.model.element.Modifier;

import com.github.benmanes.caffeine.cache.Feature;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.MethodSpec;

/**
 * @author ben.manes@gmail.com (Ben Manes)
 */
public final class AddRemovalListener implements LocalCacheRule {

  @Override
  public boolean applies(LocalCacheContext context) {
    return context.generateFeatures.contains(Feature.LISTENING);
  }

  @Override
  public void execute(LocalCacheContext context) {
    context.cache.addField(
        FieldSpec.builder(REMOVAL_LISTENER, "removalListener", Modifier.FINAL).build());
    context.constructor.addStatement("this.removalListener = builder.getRemovalListener(async)");
    context.cache.addMethod(MethodSpec.methodBuilder("removalListener")
        .addModifiers(context.publicFinalModifiers())
        .addStatement("return removalListener")
        .returns(REMOVAL_LISTENER)
        .build());
    context.cache.addMethod(MethodSpec.methodBuilder("hasRemovalListener")
        .addModifiers(context.protectedFinalModifiers())
        .addStatement("return true")
        .returns(boolean.class)
        .build());
  }
}
