//package com.stefanolupo.ndngame.names;
//
//package com.stefanolupo.ndngame.names;
//
//import net.named_data.jndn.Interest;
//import net.named_data.jndn.Name;
//import net.named_data.jndn.sync.ChronoSync2013;
//
//import java.util.regex.Pattern;
//
///**
// * Schema: base_name/|game_id|/|player_name|/blocks
// */
//public class BlockName extends BaseName {
//
//    private static final Pattern NAME_PATTERN = Pattern.compile("/\\d+/[a-z]+/blocks/\\d+");
//
//    private long gameId;
//    private String playerName;
//    private long sequenceNumber;
//
//    /**
//     * Validate and parse name incoming from Interest
//     */
////    public PlayerStatusName(Interest interest) {
////        super(interest.getName());
////        parse();
////    }
//
//    /**
//     * Validate and parse name incoming from SyncState
//     */
//    public PlayerStatusName(ChronoSync2013.SyncState syncState) {
//        super(new Name(syncState.getDataPrefix()).append(String.valueOf(syncState.getSequenceNo())));
//        parse();
//    }
//
//    /**
//     * Create a PlayerStatusName using the components
//     */
//    public PlayerStatusName(long gameId, String playerName) {
//        super(String.valueOf(gameId), playerName, "status");
//        this.gameId = gameId;
//        this.playerName = playerName;
//        this.sequenceNumber = 0;
//    }
//
//    public Name getListenName() {
//        return new Name(GAME_BASE_NAME)
//                .append(String.valueOf(gameId))
//                .append(playerName)
//                .append("status");
//    }
//
//    @Override
//    public Interest toInterest() {
//        return new Interest(getExpressInterestName());
//    }
//
//    public Name getExpressInterestName() {
//        return getListenName()
//                .append(String.valueOf(sequenceNumber));
//    }
//
//    public long getGameId() {
//        return gameId;
//    }
//
//    public String getPlayerName() {
//        return playerName;
//    }
//
//    public long getSequenceNumber() {
//        return sequenceNumber;
//    }
//
//    private void parse() {
////        Preconditions.checkArgument(tailName.size() == 4);
//        checkMatchesRegex(tailName, NAME_PATTERN);
//
//        gameId = Long.valueOf(tailName.get(0).toEscapedString());
//        playerName = tailName.get(1).toEscapedString();
//        sequenceNumber = Long.valueOf(tailName.get(3).toEscapedString());
//    }
//
//
//    /**
//     * equals which ignores the sequence number
//     */
//    @Override
//    public boolean equals(Object obj) {
//        if (!(obj instanceof PlayerStatusName)) {
//            return false;
//        }
//
//        PlayerStatusName other = (PlayerStatusName) obj;
//
//        return other.playerName.equals(playerName) &&
//                other.gameId == gameId;
//    }
//
//    /**
//     * hashCode which ignores the sequence number
//     */
//    @Override
//    public int hashCode() {
//        return 31 * playerName.hashCode() + Long.hashCode(gameId);
//    }
//}