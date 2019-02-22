package com.stefanolupo.ndngame.backend.subscriber;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.protobuf.InvalidProtocolBufferException;
import com.stefanolupo.ndngame.backend.chronosynced.OnPlayersDiscovered;
import com.stefanolupo.ndngame.backend.ndn.FaceManager;
import com.stefanolupo.ndngame.config.Config;
import com.stefanolupo.ndngame.names.BlockName;
import com.stefanolupo.ndngame.protos.Block;
import com.stefanolupo.ndngame.protos.Blocks;
import com.stefanolupo.ndngame.protos.Player;
import net.named_data.jndn.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Singleton
public class BlockSubscriber implements OnPlayersDiscovered {

    private static final Logger LOG = LoggerFactory.getLogger(BlockSubscriber.class);

    private final List<BaseSubscriber<Map<BlockName, Block>>> subscribersList = new ArrayList<>();
    private final Config config;
    private final FaceManager faceManager;

    @Inject
    public BlockSubscriber(Config config,
                           FaceManager faceManager) {
        this.config = config;
        this.faceManager = faceManager;
    }

    public void addSubscription(BlockName blockName) {
        LOG.info("Adding subscription for {}", blockName);
        BaseSubscriber<Map<BlockName, Block>> subscriber = new BaseSubscriber<>(
                faceManager,
                blockName,
                this::typeFromData,
                BlockName::new
        );
        subscribersList.add(subscriber);
    }

    public Map<BlockName, Block> getBlocksById() {
        Map<BlockName, Block> map = new HashMap<>();

        for (BaseSubscriber<Map<BlockName, Block>> subscriber : subscribersList) {
            // Can be null before first remote receipt of entity
            if (subscriber.getEntity() == null) {
                continue;
            }

            map.putAll(subscriber.getEntity());
//            for (String id : subscriber.getEntity().keySet()) {
//                Block block = subscriber.getEntity().get(id);
//                map.put(id, block);
//            }
        }

        return map;
    }


    public void interactWithBlock(String blockId) {
//        for (BaseSubscriber<Map<String, Block>> subscriber : subscribersList) {
//            if (subscriber.getEntity().containsKey(blockId)) {
//                BlockInteractionName name = new BlockInteractionName(config.getGameId(), "desktop", blockId);
//                Interest interest = name.toInterest();
//                LOG.info("Interacting with block: {}", interest.toUri());
//                faceManager.expressInterestSafe(interest);
//                return;
//            }
//        }
    }

    private Map<BlockName, Block> typeFromData(Data data) {
        try {
            List<Block> blocks = Blocks.parseFrom(data.getContent().getImmutableArray()).getBlocksList();
            return Maps.uniqueIndex(blocks, b -> {
                BlockName name = new BlockName(data);
                name.setId(b.getId());
                return name;
            });
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException("Unable to parse Block for %s" + data.getName().toUri(), e);
        }
    }

    @Override
    public void onPlayersDiscovered(Set<Player> players) {
        players.forEach(p -> this.addSubscription(new BlockName(config.getGameId(), p.getName())));
    }
}
