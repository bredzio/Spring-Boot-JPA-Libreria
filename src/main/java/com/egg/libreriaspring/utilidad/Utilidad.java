package com.egg.libreriaspring.utilidad;

import java.util.UUID;
public class Utilidad {
    
    public static String generarIsbn() {
        return UUID.randomUUID().toString()
                .replaceAll("[^0-9]", "")
                .substring(0, 13);
    }
}
