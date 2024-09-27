package org.isfpp.logica;

import java.util.Random;

public class Utils {
    /**
     * Genera una direccion MAC aleatoria, usada en la creacion de todo Nodo
     *
     * @return String
     */
    public static String generarMAC() {
        Random random = new Random();
        byte[] macAddr = new byte[6];
        random.nextBytes(macAddr);

        StringBuilder macAddress = new StringBuilder(18);
        for (byte b : macAddr) {
            if (!macAddress.isEmpty()) {
                macAddress.append(":");
            }
            macAddress.append(String.format("%02x", b));
        }
        return macAddress.toString().toUpperCase();
    }
    
}
