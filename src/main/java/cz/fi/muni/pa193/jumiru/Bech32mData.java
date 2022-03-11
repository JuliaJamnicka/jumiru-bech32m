package cz.fi.muni.pa193.jumiru;

import java.util.List;

public record Bech32mData(String hrPart, List<Byte> dataPart) {}
