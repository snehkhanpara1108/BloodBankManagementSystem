package com.bloodbank.util;

import java.sql.*;

public final class Database {
    private static Connection connection;

    private Database() {}

    public static synchronized void setup() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:bloodbank.db");
            try (Statement st = connection.createStatement()) {
                st.execute("PRAGMA foreign_keys=ON");
                st.execute("PRAGMA busy_timeout=5000");
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS donors(
                        id TEXT PRIMARY KEY, name TEXT NOT NULL, phone TEXT,
                        age INTEGER NOT NULL, weight REAL NOT NULL,
                        group_name TEXT NOT NULL, last_donation TEXT
                    )""");
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS blood_units(
                        id TEXT PRIMARY KEY, group_name TEXT NOT NULL,
                        component TEXT NOT NULL, collected_on TEXT NOT NULL,
                        expires_on TEXT NOT NULL, donor_id TEXT NOT NULL,
                        status TEXT NOT NULL
                    )""");
                st.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS requests(
                        id TEXT PRIMARY KEY, patient TEXT NOT NULL, hospital TEXT NOT NULL,
                        group_name TEXT NOT NULL, component TEXT NOT NULL, units INTEGER NOT NULL,
                        priority TEXT NOT NULL, created_at TEXT NOT NULL, status TEXT NOT NULL
                    )""");
            }
        } catch (Exception e) {
            throw new RuntimeException("Database setup failed: " + e.getMessage(), e);
        }
    }

    public static Connection connection() {
        if (connection == null) setup();
        return connection;
    }

    public static synchronized void close() {
        if (connection != null) {
            try { connection.close(); } catch (SQLException ignored) {}
            connection = null;
        }
    }
}
