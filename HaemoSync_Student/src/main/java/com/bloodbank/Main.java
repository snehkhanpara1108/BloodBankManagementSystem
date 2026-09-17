package com.bloodbank;

import com.bloodbank.dao.BloodUnitDao;
import com.bloodbank.dao.DonorDao;
import com.bloodbank.dao.RequestDao;
import com.bloodbank.service.BloodBankService;
import com.bloodbank.ui.ConsoleMenu;
import com.bloodbank.util.Database;

public class Main {
    public static void main(String[] args) {
        Database.setup();

        BloodBankService service = new BloodBankService(
                new DonorDao(), new BloodUnitDao(), new RequestDao());

        ConsoleMenu menu = new ConsoleMenu(service);

        Runtime.getRuntime().addShutdownHook(new Thread(Database::close));
        menu.start();
    }
}
