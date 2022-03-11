package cz.fi.muni.pa193.jumiru;

public class TestVectors {
    public static final String[] VALID_BECH32M = {
            "A1LQFN3A",
            "a1lqfn3a",
            "an83characterlonghumanreadablepartthatcontainsthetheexcludedcharactersbioandnumber11sg7hg6",
            "abcdef1l7aum6echk45nj3s0wdvt2fg8x9yrzpqzd3ryx",
            "11llllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllludsr8",
            "split1checkupstagehandshakeupstreamerranterredcaperredlc445v",
            "?1v759aa"
    };
    public static final String[] INVALID_BECH32M = {
            " 1xj0phk",          // HRP character out of range
            (char)0x7F + "1g6xzxy",  // HRP character out of range
            (char)0x80 + "1vctc34",  // HRP character out of range
            "an84characterslonghumanreadablepartthatcontainsthetheexcludedcharactersbioandnumber11d6pts4", // overall max length exceeded
            "qyrz8wqd2c9m",      // No separator character
            "1qyrz8wqd2c9m",     // Empty HRP
            "y1b0jsk6g",         // Invalid data character
            "lt1igcx5c0",        // Invalid data character
            "in1muywd",          // Too short checksum
            "mm1crxm3i",         // Invalid character in checksum
            "au1s5cgom",         // Invalid character in checksum
//            "M1VUXWEZ",          // Checksum calculated with uppercase form of HRP
            "16plkw9",           // Empty HRP
            "1p2gdwpf",          // Empty HRP
    };
}
