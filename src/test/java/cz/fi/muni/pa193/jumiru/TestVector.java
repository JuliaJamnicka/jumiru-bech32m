package cz.fi.muni.pa193.jumiru;

public class TestVector {
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
    public static final String[][] VALID_ADDRESS ={
            {"BC1QW508D6QEJXTDG4Y5R3ZARVARY0C5XW7KV8F3T4", "0014751e76e8199196d454941c45d1b3a323f1433bd6"},
            {"tb1qrp33g0q5c5txsp9arysrx4k6zdkfs4nce4xj0gdcccefvpysxf3q0sl5k7",
            "00201863143c14c5166804bd19203356da136c985678cd4d27a1b8c6329604903262"},
            {"bc1pw508d6qejxtdg4y5r3zarvary0c5xw7kw508d6qejxtdg4y5r3zarvary0c5xw7kt5nd6y",
            "5128751e76e8199196d454941c45d1b3a323f1433bd6751e76e8199196d454941c45d1b3a323f1433bd6"},
            {"BC1SW50QGDZ25J", "6002751e"},
            {"bc1zw508d6qejxtdg4y5r3zarvaryvaxxpcs", "5210751e76e8199196d454941c45d1b3a323"},
            {"tb1qqqqqp399et2xygdj5xreqhjjvcmzhxw4aywxecjdzew6hylgvsesrxh6hy",
            "0020000000c4a5cad46221b2a187905e5266362b99d5e91c6ce24d165dab93e86433"},
            {"tb1pqqqqp399et2xygdj5xreqhjjvcmzhxw4aywxecjdzew6hylgvsesf3hn0c",
            "5120000000c4a5cad46221b2a187905e5266362b99d5e91c6ce24d165dab93e86433"},
            {"bc1p0xlxvlhemja6c4dqv22uapctqupfhlxm9h8z3k2e72q4k9hcz7vqzk5jj0",
            "512079be667ef9dcbbac55a06295ce870b07029bfcdb2dce28d959f2815b16f81798"},
    };
    public static final String[] INVALID_ADDRESS = {
            "tc1p0xlxvlhemja6c4dqv22uapctqupfhlxm9h8z3k2e72q4k9hcz7vq5zuyut", // Invalid HRP
            "bc1p0xlxvlhemja6c4dqv22uapctqupfhlxm9h8z3k2e72q4k9hcz7vqh2y7hd", // Invalid checksum algorithm (bech32 instead of bech32m)
            "tb1z0xlxvlhemja6c4dqv22uapctqupfhlxm9h8z3k2e72q4k9hcz7vqglt7rf", // Invalid checksum algorithm (bech32 instead of bech32m)
            "BC1S0XLXVLHEMJA6C4DQV22UAPCTQUPFHLXM9H8Z3K2E72Q4K9HCZ7VQ54WELL", // Invalid checksum algorithm (bech32 instead of bech32m)
            "bc1qw508d6qejxtdg4y5r3zarvary0c5xw7kemeawh",                     // Invalid checksum algorithm (bech32m instead of bech32)
            "tb1q0xlxvlhemja6c4dqv22uapctqupfhlxm9h8z3k2e72q4k9hcz7vq24jc47", // Invalid checksum algorithm (bech32m instead of bech32)
            "bc1p38j9r5y49hruaue7wxjce0updqjuyyx0kh56v8s25huc6995vvpql3jow4", // Invalid character in checksum
            "BC130XLXVLHEMJA6C4DQV22UAPCTQUPFHLXM9H8Z3K2E72Q4K9HCZ7VQ7ZWS8R", // Invalid witness version
            "bc1pw5dgrnzv",                                                   // Invalid program length (1 byte)
            "bc1p0xlxvlhemja6c4dqv22uapctqupfhlxm9h8z3k2e72q4k9hcz7v8n0nx0muaewav253zgeav", // Invalid program length (41 bytes)
            "BC1QR508D6QEJXTDG4Y5R3ZARVARYV98GJ9P",                           // Invalid program length for witness version 0 (per BIP141)
            "tb1p0xlxvlhemja6c4dqv22uapctqupfhlxm9h8z3k2e72q4k9hcz7vq47Zagq", // Mixed case
            "bc1p0xlxvlhemja6c4dqv22uapctqupfhlxm9h8z3k2e72q4k9hcz7v07qwwzcrf", //  More than 4 padding bits
            "tb1p0xlxvlhemja6c4dqv22uapctqupfhlxm9h8z3k2e72q4k9hcz7vpggkg4j", //  Non-zero padding in 8-to-5 conversion
            "bc1gmk9yu"                                                       // Empty data section
    };
}
