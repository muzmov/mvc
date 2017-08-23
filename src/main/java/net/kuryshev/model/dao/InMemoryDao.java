package net.kuryshev.model.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import net.kuryshev.exception.BadRequestException;
import net.kuryshev.exception.ResourceNotFoundException;
import net.kuryshev.model.Data;
import net.kuryshev.model.entity.*;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryDao {
    private ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    private List<User> users = Data.getUsers().getUsers();
    private List<Location> locations = Data.getLocations().getLocations();
    private List<Visit> visits = Data.getVisits().getVisits();
    private final NumberFormat nf;

    public InMemoryDao() {
        nf = NumberFormat.getInstance();
        nf.setMaximumIntegerDigits(309);
        nf.setMinimumIntegerDigits(1);
        nf.setMaximumFractionDigits(5);
        nf.setMinimumFractionDigits(1);
        nf.setGroupingUsed(false);
    }

    public String getJsonUser(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                try {
                    return ow.writeValueAsString(user);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public User getUser(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public String getJsonVisit(int id) {
        for (Visit visit : visits) {
            if (visit.getId() == id) {
                try {
                    return ow.writeValueAsString(visit);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public Visit getVisit(int id) {
        for (Visit visit : visits) {
            if (visit.getId() == id) {
                return visit;
            }
        }
        return null;
    }

    public String getJsonLocation(int id) {
        for (Location location : locations) {
            if (location.getId() == id) {
                try {
                    return ow.writeValueAsString(location);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public Location getLocation(int id) {
        for (Location location : locations)
            if (location.getId() == id) return location;
        return null;
    }

    public String getJsonUserVisits(int id, long fromDate, long toDate, String country, int toDistance) {
        if (getUser(id) == null) throw new ResourceNotFoundException();
        ResultVisits result = new ResultVisits();
        result.setVisits(new ArrayList<>());
        for (Visit visit : this.visits) {
            if (visit.getUser() == id && visit.getVisited_at() < toDate && visit.getVisited_at() > fromDate) {
                Location location = getLocation(visit.getLocation());
                if (location.getDistance() < toDistance)
                    if (country == null || country.isEmpty() || location.getCountry().equals(country)) {
                        ResultVisit resultVisit = new ResultVisit();
                        resultVisit.setMark(visit.getMark());
                        resultVisit.setPlace(location.getPlace());
                        resultVisit.setVisited_at(visit.getVisited_at());
                        result.getVisits().add(resultVisit);
                    }
            }
        }
        //if (result.getVisits().isEmpty()) throw new ResourceNotFoundException();
        result.getVisits().sort((o1, o2) -> (int) (o1.getVisited_at() - o2.getVisited_at()));
        try {
            return ow.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ResourceNotFoundException();
        }
    }

    public String getJsonLocationAvg(int id, long fromDate, long toDate, long fromAge, long toAge, String gender) {
        if (getLocation(id) == null) throw new ResourceNotFoundException();
        double result = 0;
        int count = 0;
        for (Visit visit : visits) {
            if (visit.getLocation() == id) {
                if (visit.getVisited_at() > fromDate && visit.getVisited_at() < toDate) {
                    User user = getUser(visit.getUser());
                    if (gender.isEmpty() || gender.equals(user.getGender())) {
                        long age = (System.currentTimeMillis() / 1000 - user.getBirth_date()) / (60 * 60 * 24 * 365);
                        if (age >= fromAge && age < toAge) {
                            count++;
                            result += visit.getMark();
                        }
                    }
                }
            }
        }
        System.out.println(result);
        System.out.println(count);
        if (count > 0) result = result / count;
        Avg avg = new Avg();
        avg.setAvg(result);
        try {
            return ow.writeValueAsString(avg);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ResourceNotFoundException();
        }
    }

    public void addUser(User user) {
        if (!checkNewUser(user)) throw new BadRequestException();
        users.add(user);
    }

    public void addLocation(Location location) {
        if (!checkNewLocation(location)) throw new BadRequestException();
        locations.add(location);
    }

    public void addVisit(Visit visit) {
        if (!checkNewVisit(visit)) throw new BadRequestException();
        visits.add(visit);
    }

    public void updateUser(int id, User user) {
        User foundUser = getUser(id);
        if (foundUser == null) throw new ResourceNotFoundException();
        if (!checkUser(user)) throw new BadRequestException();
        if (user.getId() != -1) throw new BadRequestException();

        if (user.getFirst_name() != null) foundUser.setFirst_name(user.getFirst_name());
        if (user.getLast_name() != null) foundUser.setLast_name(user.getLast_name());
        if (user.getBirth_date() != Long.MIN_VALUE) foundUser.setBirth_date(user.getBirth_date());
        if (user.getEmail() != null) foundUser.setEmail(user.getEmail());
        if (user.getGender() != null) foundUser.setGender(user.getGender());

    }

    public void updateLocation(int id, Location location) {
        Location foundLocation = getLocation(id);
        if (foundLocation == null) throw new ResourceNotFoundException();
        if (!checkLocation(location)) throw new BadRequestException();
        if (location.getId() != -1) throw new BadRequestException();

        if (location.getCountry() != null) foundLocation.setCountry(location.getCountry());
        if (location.getPlace() != null) foundLocation.setPlace(location.getPlace());
        if (location.getCity() != null) foundLocation.setCity(location.getCity());
        if (location.getDistance() != -1) foundLocation.setDistance(location.getDistance());
    }

    public void updateVisit(int id, Visit visit) {
        Visit foundVisit = getVisit(id);
        if (foundVisit == null) throw new ResourceNotFoundException();
        if (!checkVisit(visit)) throw new BadRequestException();
        if (visit.getId() != -1) throw new BadRequestException();

        if (visit.getVisited_at() != Long.MIN_VALUE) foundVisit.setVisited_at(visit.getVisited_at());
        if (visit.getUser() != -1) foundVisit.setUser(visit.getUser());
        if (visit.getLocation() != -1) foundVisit.setLocation(visit.getLocation());
        if (visit.getMark() != -1) foundVisit.setMark(visit.getMark());
    }


    private boolean checkNewUser(User user) {
        if (user == null) return false;
        if (user.getId() == -1) return false;
        if (user.getBirth_date() == Long.MIN_VALUE) return false;
        if (user.getFirst_name() == null) return false;
        if (user.getLast_name() == null) return false;
        if (user.getGender() == null) return false;
        if (user.getEmail() == null) return false;
        if (getUser(user.getId()) != null) return false;
        return checkUser(user);
       // return true;
    }

    private boolean checkUser(User user) {
        if (user == null) return false;
        if (user.getBirth_date() != Long.MIN_VALUE && (user.getBirth_date() < -1262304000 || user.getBirth_date() > 915148800)) return false;
        if (user.getFirst_name() != null && (user.getFirst_name().length() > 50)) return false;
        if (user.getLast_name() != null && (user.getLast_name().length() > 50)) return false;
        if (user.getGender() != null && !(user.getGender().equals("f") || user.getGender().equals("m"))) return false;
        if (user.getEmail() != null && user.getEmail().length() > 100) return false;
        return true;
    }

    private boolean checkNewLocation(Location location) {
        if (location == null) return false;
        if (location.getId() == -1) return false;
        if (location.getCountry() == null) return false;
        if (location.getPlace() == null) return false;
        if (location.getCity() == null) return false;
        if (location.getDistance() == -1) return false;
        if (getLocation(location.getId()) != null) return false;
        return checkLocation(location);
       // return true;
    }

    private boolean checkLocation(Location location) {
        if (location == null) return false;
        if (location.getCountry() != null && (location.getCountry().length() > 50 || location.getCountry().isEmpty())) return false;
        if (location.getPlace() != null && location.getPlace().isEmpty()) return false;
        if (location.getCity() != null &&  (location.getCity().length() > 50 || location.getCity().isEmpty())) return false;
        if (location.getDistance() != -1 && location.getDistance() < 0) return false;
        return true;
    }

    private boolean checkNewVisit(Visit visit) {
        if (visit == null) return false;
        if (visit.getId() == -1) return false;
        if (visit.getVisited_at() == Long.MIN_VALUE) return false;
        if (visit.getUser() == -1) return false;
        if (visit.getLocation() == -1) return false;
        if (getVisit(visit.getId()) != null) return false;
        if (visit.getMark() == -1) return false;
      //  return true;
        return checkVisit(visit);
    }

    private boolean checkVisit(Visit visit) {
        if (visit == null) return false;
        if (visit.getVisited_at() != Long.MIN_VALUE && (visit.getVisited_at() > 1420070400 || visit.getVisited_at() < 946684800)) return false;
      //  if (visit.getUser() != -1 && getUser(visit.getUser()) == null) return false;
       // if (visit.getLocation() != -1 && getLocation(visit.getLocation()) == null) return false;
        if (visit.getMark() != -1 && (visit.getMark() > 5 || visit.getMark() < 0)) return false;
        return true;
    }
}
