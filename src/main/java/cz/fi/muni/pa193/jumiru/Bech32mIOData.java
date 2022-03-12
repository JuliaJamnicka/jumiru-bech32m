package cz.fi.muni.pa193.jumiru;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Bech32mIOData {
    private final String hrPart;
    private final List<Byte> dataPart;

    public Bech32mIOData(String hrPart, List<Byte> dataPart) {
        this.hrPart = hrPart;
        this.dataPart = dataPart;
    }

    public Bech32mIOData(String hrPart, String hexDataPart) {
        this.hrPart = hrPart;
        this.dataPart = convertHexToBytes(hexDataPart);
    }

    private List<Byte> convertHexToBytes(String hex) {
        ArrayList<Byte> bytes = new ArrayList<>();
        if (hex.length() % 2 == 1) {
            hex = hex.concat("0");
        }
        bytes.ensureCapacity(hex.length() / 2);
        for (int i = 0; i < hex.length(); i += 2) {
            bytes.add(Byte.parseByte(hex.substring(i, i + 2), 16));
        }
        return bytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bech32mIOData that = (Bech32mIOData) o;
        return Objects.equals(hrPart, that.hrPart) && Objects.equals(dataPart, that.dataPart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hrPart, dataPart);
    }

    public String getHrPart() {
        return hrPart;
    }

    public List<Byte> getDataPart() {
        return Collections.unmodifiableList(dataPart);
    }
}
