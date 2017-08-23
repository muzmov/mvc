package net.kuryshev.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.kuryshev.model.entity.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Data {
    private static Users users;
    private static Locations locations;
    private static Visits visits;

    public static final String PREFIX = "C:\\Users\\1\\Desktop\\mvc\\src\\main\\resources\\data\\";
//    public static final String PREFIX = "/tmp/data/";

    static {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(System.getProperty("user.dir"));
        users = new Users();
        users.setUsers(Collections.synchronizedList(new ArrayList<>()));
        locations = new Locations();
        locations.setLocations(Collections.synchronizedList(new ArrayList<>()));
        visits = new Visits();
        visits.setVisits(Collections.synchronizedList(new ArrayList<>()));

        try (ZipFile zipFile = new ZipFile(PREFIX + "data.zip")) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                InputStream stream = zipFile.getInputStream(entry);
                if (entry.getName().matches(".*users.*")) {
                    Users tempUsers = mapper.readValue(stream, Users.class);
                    users.getUsers().addAll(tempUsers.getUsers());
                }
                if (entry.getName().matches(".*locations.*")) {
                    Locations tempLocations = mapper.readValue(stream, Locations.class);
                    locations.getLocations().addAll(tempLocations.getLocations());
                }
                if (entry.getName().matches(".*visits.*")) {
                    Visits tempVisits = mapper.readValue(stream, Visits.class);
                    visits.getVisits().addAll(tempVisits.getVisits());
                }
            }
        }
        catch (IOException e) {
            System.out.println("No such file. Current dir is " + System.getProperty("user.dir"));
            e.printStackTrace();
        }
        System.out.println("Data loaded");
    }

    public static Users getUsers() {
        return users;
    }

    public static Locations getLocations() {
        return locations;
    }

    public static Visits getVisits() {
        return visits;
    }
}

