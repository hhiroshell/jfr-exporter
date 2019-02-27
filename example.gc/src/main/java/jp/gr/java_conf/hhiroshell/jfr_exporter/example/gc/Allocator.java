/*
 * Copyright (c) 2019 Hiroshi Hayakawa <hhiroshell@gmail.com>
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

package jp.gr.java_conf.hhiroshell.jfr_exporter.example.gc;

import java.util.Collection;
import java.util.HashMap;

public class Allocator {
    // Integer -> MyAlloc
    private HashMap<Integer, Allocatee> map = new HashMap<>();

    private static class Allocatee {
        private int id;

        private Allocatee(int id) {
            this.id = id;
        }

        private int getId() {
            return id;
        }
    }

    public static void main(String[] args) {
        new Allocator().go();
    }

    private void go() {
        alloc(10000);
        long yieldCounter = 0;
        while(true) {
            Collection<Allocatee> allocatees = map.values();
            for (Allocatee c : allocatees) {
                if (!map.containsKey(c.getId())) {
                    System.out.println("Now this is strange!");
                }
                if (++yieldCounter % 1000 == 0) {
                    Thread.yield();
                }
            }
        }
    }

    private void alloc(int count) {
        for (int i = 0; i < count; i++) {
            map.put(i, new Allocatee(i));
        }
    }
}
