package com.lib.util;

import com.lib.dao.AdminDao;
import com.lib.entity.Admin;

import jakarta.persistence.EntityManager;

import java.util.Random;

public class AdminIdAssigner {
    
    private static final Random random = new Random();

   
    public static String assignUniqueId(Admin admin) {
    	
        String name = admin.getName();
        
        String cleanName = name.replaceAll("\\s+", "").toUpperCase();
        
        String namePart;
        if (cleanName.length() >= 3) {
            namePart = cleanName.substring(0, 3);
        } else {
            namePart = (cleanName + "XXX").substring(0, 3);
        }
        
        String finalId;
        boolean isUnique = false;

        do {
            int number = random.nextInt(9000) + 1000;
            finalId = "AD" + namePart + number;

            if (!(AdminDao.existsByMembershipNo(finalId))) {
                isUnique = true;
            }
        } while (!isUnique);

 return finalId;
    }
}