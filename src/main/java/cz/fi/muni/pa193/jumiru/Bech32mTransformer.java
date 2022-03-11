package cz.fi.muni.pa193.jumiru;

import java.util.List;

public interface Bech32mTransformer {

    boolean checkBech32mString(String str);

    boolean verifyChecksum(String hrPart, List<Byte> dataPart);

    Bech32mData decodeBech32mString(String str);
}
