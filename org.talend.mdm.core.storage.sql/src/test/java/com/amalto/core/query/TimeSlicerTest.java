package com.amalto.core.query;

import com.amalto.core.query.user.Select;
import com.amalto.core.query.user.TimeSlicer;
import com.amalto.core.query.user.UserQueryBuilder;
import com.amalto.core.storage.record.DataRecordReader;
import com.amalto.core.storage.record.XmlStringDataRecordReader;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import static com.amalto.core.query.user.UserQueryBuilder.*;

/**
 *
 */
public class TimeSlicerTest extends StorageTestCase {

    public void testArguments() throws Exception {
        Select select = UserQueryBuilder.from(person).getSelect();
        Iterator<TimeSlicer.Slice> slice = TimeSlicer.slice(null, null, 0, ((TimeUnit) null));
        assertFalse(slice.hasNext());
        slice = TimeSlicer.slice(null, storage, 0, TimeUnit.DAYS);
        assertFalse(slice.hasNext());
        try {
            TimeSlicer.slice(select, null, 0, TimeUnit.HOURS);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            TimeSlicer.slice(select, storage, -1, TimeUnit.DAYS);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    public void testSlice1() throws Exception {
        DataRecordReader<String> factory = new XmlStringDataRecordReader();
        for (int i = 0; i < 6; i++) {
            storage.begin();
            storage.update(factory
                    .read(
                            repository,
                            country,
                            "<Country><id>" + i + "</id><creationDate>2010-10-10</creationDate><creationTime>2010-10-10T00:00:01</creationTime><name>France</name></Country>"));
            storage.commit();
            Thread.sleep(200);
        }
        UserQueryBuilder qb = UserQueryBuilder.from(country);
        // Periods of 200 ms
        storage.begin();
        Iterator<TimeSlicer.Slice> slice = TimeSlicer.slice(qb.getSelect(), storage, 200, TimeUnit.MILLISECONDS);
        int count = 0;
        while(slice.hasNext()) {
            slice.next();
            count++;
        }
        storage.commit();
        assertTrue(count - 6 <= 1); // Time based assertions aren't really precise, consider deviation of > 1 as problem
        // Periods of 400 ms
        storage.begin();
        slice = TimeSlicer.slice(qb.getSelect(), storage, 400, TimeUnit.MILLISECONDS);
        count = 0;
        while(slice.hasNext()) {
            slice.next();
            count++;
        }
        storage.commit();
        assertTrue(count - 3 <= 1); // Time based assertions aren't really precise, consider deviation of > 1 as problem
        // Periods of 1200 ms
        storage.begin();
        slice = TimeSlicer.slice(qb.getSelect(), storage, 1200, TimeUnit.MILLISECONDS);
        count = 0;
        while(slice.hasNext()) {
            slice.next();
            count++;
        }
        storage.commit();
        assertTrue(count - 1 <= 1); // Time based assertions aren't really precise, consider deviation of > 1 as problem
    }

    public void testSlice2() throws Exception {
        DataRecordReader<String> factory = new XmlStringDataRecordReader();
        for (int i = 0; i < 6; i++) {
            storage.begin();
            storage.update(factory
                    .read(
                            repository,
                            country,
                            "<Country><id>"
                                    + i
                                    + "</id><creationDate>2010-10-10</creationDate><creationTime>2010-10-10T00:00:01</creationTime><name>France</name></Country>"));
            storage.commit();
            Thread.sleep(200);
        }
        UserQueryBuilder qb = UserQueryBuilder.from(country);
        // Max number of slice: 10
        storage.begin();
        Iterator<TimeSlicer.Slice> slice = TimeSlicer.slice(qb.getSelect(), storage, 10, timestamp());
        int count = 0;
        while (slice.hasNext()) {
            slice.next();
            count++;
        }
        storage.commit();
        assertTrue(count <= 10);
        // Max number of slice: 5
        storage.begin();
        slice = TimeSlicer.slice(qb.getSelect(), storage, 5, timestamp());
        count = 0;
        while (slice.hasNext()) {
            slice.next();
            count++;
        }
        storage.commit();
        assertTrue(count <= 5);
        // Max number of slice: 1
        storage.begin();
        slice = TimeSlicer.slice(qb.getSelect(), storage, 1, timestamp());
        count = 0;
        while (slice.hasNext()) {
            slice.next();
            count++;
        }
        storage.commit();
        assertTrue(count == 1);
        // Max number of slice: -1
        storage.begin();
        slice = TimeSlicer.slice(qb.getSelect(), storage, -1, timestamp());
        count = 0;
        while (slice.hasNext()) {
            slice.next();
            count++;
        }
        storage.commit();
        assertTrue(count == 0);
        // Max number of slice: -1
        storage.begin();
        slice = TimeSlicer.slice(qb.getSelect(), storage, 0, timestamp());
        count = 0;
        while (slice.hasNext()) {
            slice.next();
            count++;
        }
        storage.commit();
        assertTrue(count == 0);
    }
    
    public void testQueryLowerBound() throws Exception {
        DataRecordReader<String> factory = new XmlStringDataRecordReader();
        long testStart = System.currentTimeMillis();
        for (int i = 0; i < 6; i++) {
            storage.begin();
            storage.update(factory
                    .read(
                            repository,
                            country,
                            "<Country><id>" + i + "</id><creationDate>2010-10-10</creationDate><creationTime>2010-10-10T00:00:01</creationTime><name>France</name></Country>"));
            storage.commit();
            Thread.sleep(200);
        }
        UserQueryBuilder qb = UserQueryBuilder.from(country).where(gte(timestamp(), String.valueOf(testStart + 800)));
        // Periods of 200 ms
        storage.begin();
        Iterator<TimeSlicer.Slice> slice = TimeSlicer.slice(qb.getSelect(), storage, 200, TimeUnit.MILLISECONDS);
        int count = 0;
        while(slice.hasNext()) {
            slice.next();
            count++;
        }
        storage.commit();
        assertTrue(count < 6);
    }

    public void testQueryUpperBound() throws Exception {
        DataRecordReader<String> factory = new XmlStringDataRecordReader();
        for (int i = 0; i < 6; i++) {
            storage.begin();
            storage.update(factory
                    .read(
                            repository,
                            country,
                            "<Country><id>" + i + "</id><creationDate>2010-10-10</creationDate><creationTime>2010-10-10T00:00:01</creationTime><name>France</name></Country>"));
            storage.commit();
            Thread.sleep(200);
        }
        long testEnd = System.currentTimeMillis();
        UserQueryBuilder qb = UserQueryBuilder.from(country).where(lte(timestamp(), String.valueOf(testEnd - 800)));
        // Periods of 200 ms
        storage.begin();
        Iterator<TimeSlicer.Slice> slice = TimeSlicer.slice(qb.getSelect(), storage, 200, TimeUnit.MILLISECONDS);
        int count = 0;
        while(slice.hasNext()) {
            slice.next();
            count++;
        }
        storage.commit();
        assertTrue(count < 6);
    }

    @Override
    public void tearDown() throws Exception {
        try {
            storage.begin();
            {
                UserQueryBuilder qb = from(country);
                storage.delete(qb.getSelect());
            }
            storage.commit();
        } finally {
            storage.end();
        }

    }
}
