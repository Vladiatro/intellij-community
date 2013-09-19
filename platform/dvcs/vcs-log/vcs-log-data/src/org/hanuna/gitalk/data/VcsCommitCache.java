/*
 * Copyright 2000-2013 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hanuna.gitalk.data;

import com.intellij.util.containers.SLRUMap;
import com.intellij.vcs.log.CommitParents;
import com.intellij.vcs.log.Hash;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * <p>The cache of commit details.</p>
 * <p>It is not actually a cache, but rather a limited map, because there is intentionally no way to get the non-cached value if it was not
 *    found in the cache: such functionality is implemented by the {@link DataGetter} which is able to receive
 *    non-cached details more efficiently, in a batch.</p>
 * <p>Any access to the Cache MUST be performed from the EDT thread.</p>
 *
 * @author Kirill Likhodedov
 */
class VcsCommitCache<T extends CommitParents> {

  private final SLRUMap<Hash, T> myCache = new SLRUMap<Hash, T>(5000, 5000);

  public void put(@NotNull Hash hash, @NotNull T commit) {
    assert EventQueue.isDispatchThread();
    myCache.put(hash, commit);
  }

  public boolean isKeyCached(@NotNull Hash hash) {
    assert EventQueue.isDispatchThread();
    return myCache.get(hash) != null;
  }

  @Nullable
  public T get(@NotNull Hash hash) {
    assert EventQueue.isDispatchThread();
    return myCache.get(hash);
  }
}
