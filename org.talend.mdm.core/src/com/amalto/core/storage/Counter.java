/*
 * Copyright (C) 2006-2017 Talend Inc. - www.talend.com
 *
 * This source code is available under agreement available at
 * %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 *
 * You should have received a copy of the agreement along with this program; if not, write to Talend SA 9 rue Pages
 * 92150 Suresnes, France
 */

package com.amalto.core.storage;

import java.util.HashSet;
import java.util.Set;

import com.amalto.core.query.user.Condition;
import com.amalto.core.query.user.Select;

public interface Counter {

    /**
     * Get entity's count data from cache
     * 
     * @param key
     * @param value
     */
    Integer get(CountKey key);

    /**
     * Put entity's count data to cache
     * 
     * @param key
     * @param value
     */
    void put(CountKey key, Integer value);

    /**
     * Clear entity's count data from cache
     * 
     * @param key
     */
    void clear(CountKey key);

    /**
     * Clear all count data from cache
     * 
     */
    void clearAll();

    public static class CountKey {

        private String storageName;

        private StorageType storageType;

        private String entityName;

        private Condition condition;

        public CountKey(Storage storage, Select select) {
            this(storage.getName(), storage.getType(), select.getTypes().get(0).getName(), select.getCondition());
        }

        public CountKey(String storageName, StorageType storageType, String entityName, Condition condition) {
            this(storageName, storageType, entityName);
            this.condition = condition;
        }

        public CountKey(String storageName, StorageType storageType, String entityName) {
            this.storageName = storageName;
            this.storageType = storageType;
            this.entityName = entityName;
        }

        public Condition getCondition() {
            return condition;
        }

        public String getEntityName() {
            return entityName;
        }

        public String getStorageName() {
            return storageName;
        }

        public StorageType getStorageType() {
            return storageType;
        }

        @Override
        public String toString() {
            int conditionHash = condition != null ? condition.hashCode() : 0;
            return getEntityKey() + conditionHash;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof CountKey)) {
                return false;
            }
            CountKey anotherKey = (CountKey) o;
            return toString().equals(anotherKey.toString());
        }

        @Override
        public int hashCode() {
            return toString().hashCode();
        }

        public String getEntityKey() {
            return storageName + '#' + storageType + '.' + entityName + '/';
        }
    }

    public static class CountKeyList {

        private static ThreadLocal<Set<CountKey>> threadLocal = new ThreadLocal<Set<CountKey>>() {

            public Set<CountKey> initialValue() {
                return new HashSet<CountKey>();
            }
        };

        private CountKeyList() {
        }

        public static Set<CountKey> get() {
            return threadLocal.get();
        }

        public static void remove() {
            threadLocal.remove();
        }
    }

}
