package com.stefanolupo.ndngame.names.blocks;

import net.named_data.jndn.Data;
import net.named_data.jndn.Interest;
import net.named_data.jndn.Name;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class BlockSyncNameTest {

    private static final String BASE_PREFIX = "/com/stefanolupo/ndngame/0/test/blocks/sync";
    private static final String FULL_NAME = String.format(BASE_PREFIX + "/%d/%d", 0, 99);
    private static final Name EXPECTED_INTEREST_NAME = new Name(BASE_PREFIX + "/99");

    @Test
    public void itShouldProduceCorrectPrefixes() {
        BlocksSyncName name = new BlocksSyncName(0, "test");
        name.setNextSequenceNumber(99);
        assertEquals(new Name(BASE_PREFIX), name.getAsPrefix());
        assertEquals(FULL_NAME, name.getFullName().toUri());
        assertEquals(EXPECTED_INTEREST_NAME, name.buildInterest().getName());
    }

    @Test
    public void itShouldParseInterestNames() {
        BlocksSyncName name = new BlocksSyncName(new Interest(new Name(BASE_PREFIX + "/0")));
        name.setNextSequenceNumber(99);

        assertEquals(new Name(FULL_NAME), name.getFullName());
    }

    @Test
    public void itShouldParseFromData() {
        BlocksSyncName blocksSyncName = new BlocksSyncName(new Data(new Name(BASE_PREFIX + "/5/99")));
        assertEquals(5, blocksSyncName.getLatestSequenceNumberSeen());
        Name name = new Name(String.format(BASE_PREFIX + "/%d/%d", 5, 99));
        assertEquals(name, blocksSyncName.getFullName());
    }
}