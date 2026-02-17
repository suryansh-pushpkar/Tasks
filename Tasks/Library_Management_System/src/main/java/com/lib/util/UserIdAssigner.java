package com.lib.util;

import java.util.Random;

import com.lib.dao.AdminDao;
import com.lib.entity.User;

public class UserIdAssigner {
	private static final Random random = new Random();

	public static String assignUniqueId(User user) {

		String name = user.getName();

		String cleanName = name.replaceAll("\\s+", "").toUpperCase();

		String namePart;
		if (cleanName.length() >= 4) {
			namePart = cleanName.substring(0, 4);
		} else {
			namePart = (cleanName + "XXXX").substring(0, 4);
		}

		String finalId;
		boolean isUnique = false;

		do {
			int number = random.nextInt(9000) + 1000;
			finalId = "USER" + namePart + number;

			if (!(AdminDao.existsByMembershipNo(finalId))) {
				isUnique = true;
			}
		} while (!isUnique);

		return finalId;
	}
}
