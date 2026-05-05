package org.example.pcbuilderapplication;

public class CompatibilityChecker {

    public static boolean socketsMatch(String cpuSocket, String motherboardSocket) {
        if (cpuSocket == null || motherboardSocket == null) {
            return false;
        }

        return cpuSocket.equals(motherboardSocket);
    }

    public static boolean ramTypesMatch(String ramType, String motherboardRamType) {
        if (ramType == null || motherboardRamType == null) {
            return false;
        }

        return ramType.equals(motherboardRamType);
    }
}