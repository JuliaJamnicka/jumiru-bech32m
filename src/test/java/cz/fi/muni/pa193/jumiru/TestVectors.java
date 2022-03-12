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

    public static final Bech32mIOData[] VALID_BECH32_DECODINGS = {
            new Bech32mIOData("a", ""),
            new Bech32mIOData("a", ""),
            new Bech32mIOData("an83characterlonghumanreadablepartthatcontainsthetheexcludedcharacters" +
                    "bioandnumber1", ""),
            new Bech32mIOData("abcdef", "1f1e1d1c1b1a191817161514131211100f0e0d0c0b0a090807" +
                    "06050403020100"),
            new Bech32mIOData("1", "1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1" +
                    "f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1f1" +
                    "f1f1f1f1f1f1f1f1f1f1f1f1f"),
            new Bech32mIOData("split", "18171918161c01100b1d0819171d130d10171d16191c01100b0" +
                    "3191d1b1903031d130b190303190d181d01190303190d"),
            new Bech32mIOData("?", "")
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
            "16plkw9",           // Empty HRP
            "1p2gdwpf",          // Empty HRP
    };

    public static final String[] INVALID_BECH32M_CHECKSUM = {
            "M1VUXWEZ",          // Checksum calculated with uppercase form of HRP
    };
}
