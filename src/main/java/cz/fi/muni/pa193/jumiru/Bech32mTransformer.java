package cz.fi.muni.pa193.jumiru;

import java.util.List;

public interface Bech32mTransformer {

    boolean checkBech32mString(String str);

    boolean verifyChecksum(String hrPart, List<Byte> dataPart);

    Bech32mInputData decodeBech32mString(String str);

    String encodeBech32mString(Bech32mInputData input);

    List<Byte> calculateChecksum(Bech32mInputData input);
}
