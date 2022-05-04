package cz.fi.muni.pa193.jumiru.bech32m;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Representation of input data to be encoded/output data that were decoded.
 */
public final class Bech32mIOData {

    /**
     * Human-readable part of data.
     */
    private final String hrPart;

    /**
     * Data part of data.
     */
    private final List<Byte> dataPart;

    public Bech32mIOData(String hrPart, List<Byte> dataPart) {
        this.hrPart = hrPart;
        this.dataPart = new ArrayList<>(dataPart);
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
