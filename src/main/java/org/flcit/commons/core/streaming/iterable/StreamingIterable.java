/*
 * Copyright 2002-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.flcit.commons.core.streaming.iterable;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;

import org.flcit.commons.core.util.BooleanUtils;
import org.flcit.commons.core.util.CollectionUtils;

/**
 * @param <T>
 * @since 
 * @author Florian Lestic
 */
public class StreamingIterable<T> implements Iterable<T> {

    private final Function<Long, List<T>> function;
    private final Supplier<Boolean> shutdown;

    /**
     * @param function
     * @param shutdown
     */
    public StreamingIterable(Function<Long, List<T>> function, Supplier<Boolean> shutdown) {
        this.function = function;
        this.shutdown = shutdown;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            private List<T> list;
            private boolean end;
            private int i = -1;
            private long offset;

            @Override
            public boolean hasNext() {
                if (end) {
                    return false;
                }
                if (list == null || i >= list.size() - 1) {
                    nextList();
                    return hasNext();
                }
                return true;
            }

            @Override
            public T next() {
                if (end) {
                    throw new NoSuchElementException();
                }
                i++;
                if (list == null || i >= list.size()) {
                    nextList();
                    return next();
                }
                return list.get(i);
            }

            private void nextList() {
                i = -1;
                if (BooleanUtils.isTrue(shutdown)) {
                    end = true;
                    return;
                }
                list = function.apply(offset);
                end = CollectionUtils.isEmpty(list);
                if (!end) {
                    offset += list.size();
                }
            }
        };
    }

}
