/**
 * Copyright 2015 The Javaslang Authors
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
package javaslang.jackson.datatype.serialize;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import javaslang.collection.Multimap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

class MultimapSerializer extends ValueSerializer<Multimap<?, ?>> {

    private static final long serialVersionUID = 1L;

    MultimapSerializer(JavaType type) {
        super(type);
    }

    @Override
    Object toJavaObj(Multimap<?, ?> value) throws IOException {
        final LinkedHashMap<Object, List<Object>> result = new LinkedHashMap<>();
        value.forEach(e -> {
            List<Object> list = result.get(e._1);
            if (list == null) {
                list = new ArrayList<>();
                result.put(e._1, list);
            }
            list.add(e._2);
        });
        return result;
    }

    @Override
    JavaType emulatedJavaType(JavaType type) {
        return MapType.construct(LinkedHashMap.class, type.containedType(0), CollectionType.construct(ArrayList.class, type.containedType(1)));
    }

    @Override
    public boolean isEmpty(SerializerProvider provider, Multimap<?, ?> value) {
        return value.isEmpty();
    }
}
