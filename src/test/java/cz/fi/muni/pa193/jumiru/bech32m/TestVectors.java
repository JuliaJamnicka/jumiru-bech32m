package cz.fi.muni.pa193.jumiru.bech32m;

import cz.fi.muni.pa193.jumiru.converter.DataInputConverter;
import cz.fi.muni.pa193.jumiru.converter.InputConverter;

class TestVectors {
    private static final InputConverter INPUT_CONVERTER = new DataInputConverter();

    static final String[] VALID_BECH32M = {
            "abcdef140x77khk82w",
            "test1wejkxar0wg64ekuu",
            "A1LQFN3A",
            "a1lqfn3a",
            "an83characterlonghumanreadablepartthatcontainsthetheexcludedcharactersbioandnumber11sg7hg6",
            "abcdef1l7aum6echk45nj3s0wdvt2fg8x9yrzpqzd3ryx",
            "?1v759aa",
            "split1checkupstagehandshakeupstreamerranterredcaperredlc445v"
    };

    static final Bech32mIOData[] VALID_BECH32_DECODINGS = {
            new Bech32mIOData("abcdef", INPUT_CONVERTER.convertFromHex("abcdef")),
            new Bech32mIOData("test", INPUT_CONVERTER.convertFromHex("766563746f72")),
            new Bech32mIOData("a", INPUT_CONVERTER.convertFromHex("")),
            new Bech32mIOData("a", INPUT_CONVERTER.convertFromHex("")),
            new Bech32mIOData("an83characterlonghumanreadablepartthatcontainsthetheexcludedcharacters" +
                    "bioandnumber1", INPUT_CONVERTER.convertFromHex("")),
            new Bech32mIOData("abcdef", INPUT_CONVERTER.convertFromHex("ffbbcdeb38bdab49ca" +
                    "307b9ac5a928398a418820")),
            new Bech32mIOData("?", INPUT_CONVERTER.convertFromHex("")),
            new Bech32mIOData("split", INPUT_CONVERTER.convertFromHex("c5f38b70305f519bf66d" +
                    "85fb6cf03058f3dde463ecd7918f2dc743918f2d"))
    };

    static final String[] VALID_CORRECTED_BECH32M = {
            "A1LQFN3A",
            "a1lqfn3a",
            "an83characterlonghumanreadablepartthatcontainsthetheexcludedcharactersbioandnumber11sg7hg6",
            "abcdef140x77khk82w",
            "abcdef1l7aum6echk45nj3s0wdvt2fg8x9yrzpqzd3ryx",
            "11llllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllludsr8",
            "split1checkupstagehandshakeupstreamerranterredcaperredlc445v",
            "?1v759aa",
            "split1checkupstagehandshakeupstreamerranterredcaperredlc445v",
            "?1v759aa",
            "an83characterlonghumanreadablepartthatcontainsthetheexcludedcharactersbioandnumber11sg7hg6",
            "abcdef140x77khk82w"
    };

    static final String[] INVALID_CORRECTS_TO_VALID_BECH32M = {
            // error in hrp
            "B1LQFN3A",
            "b1lqfn3a",
            "an84characterlonghumanreadablepartthatcontainsthetheexcludedcharactersbioandnumber11sg7hg6",
            "zbcdef140x77khk82w",
            // error in data part
            "abcdef1p7aum6echk45nj3s0wdvt2fg8x9yrzpqzd3ryx",
            "11lllllllllllllllllllllllllllllllllllllllllllllllllllrllllllllllllllllllllllllllllllludsr8",
            "split1cheqkupstagehandshakeupstreamerranterredcaperredlc445v",
            "!1v759aa",
            // error in data part (checksum)
            "split1checkupstagehandshakeupstreamerranterredcaperredlc845v",
            "?1v759az",
            "an83characterlonghumanreadablepartthatcontainsthetheexcludedcharactersbioandnumber11sg7h56",
            "abcdef140x77khk82p"
    };

    static final String[] INVALID_BECH32M = {
            " 1xj0phk",          // HRP character out of range
            (char)0x7F + "1g6xzxy",  // HRP character out of range
            (char)0x80 + "1vctc34",  // HRP character out of range
            "an84characterslonghumanreadablepartthatcontainsthetheexcludedcharactersbioandnumber11d6pts4", // too long hrp
            "an84characterslonghumanreadablepartthatcontainsthetheexcludedcharactersbioandnumbe11pd6pts4", // overall max length exceeded
            "qyrz8wqd2c9m",      // No separator character
            "1qyrz8wqd2c9m",     // Empty HRP
            "y1b0jsk6g",         // Invalid data character
            "lt1igcx5c0",        // Invalid data character
            "in1muywd",          // Too short checksum
            "mm1crxm3i",         // Invalid character in checksum
            "au1s5cgom",         // Invalid character in checksum
            "16plkw9",           // Empty HRP
            "1p2gdwpf",          // Empty HRP
            // additional test vectors
            "lT1igcx5c0",        // Mixed case HRP
            "abcdef1l7auM6echk45nj3s0wdvt2fg8x9yrzpqzd3ryx",  // Mixed case data part
    };

    static final String[] INVALID_BECH32M_CHECKSUM = {
            "M1VUXWEZ",          // Checksum calculated with uppercase form of HRP
            // valid bech32m strings with modified checksum
            "an83characterlonghumanreadablepartthatcontainsthetheexcludedcharactersbioandnumber11sg7hg5",
            "11llllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllluusr8",
            "?1v759la",
            "abcdef140x77khk8hw",
            "a1lqfffa",
            // valid bech32m strings with modified hrp
            "b1lqfn3a",
            "an66characterlonghumanreadablepartthatcontainsthetheexcludedcharactersbioandnumber11sg7hg6",
            "zbcdef1l7aum6echk45nj3s0wdvt2fg8x9yrzpqzd3ryx",
            "61llllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllludsr8",
            "!1v759aa",
            // valid bech32m string with modified data part (not checksum)
            "abcdef1l7aum6echk55nj3s0wdvt2fg8x9yrzpqzd3ryx",
            "11lllllllllllllllrrrllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllludsr8",
            "split1cheqkupstagehandshakeupstreamerranterredcaperredlc445v",
            "abcdef140y77khk82w"
    };
}
