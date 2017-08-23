package net.kuryshev.controller;

import net.kuryshev.exception.BadRequestException;
import net.kuryshev.exception.ResourceNotFoundException;
import net.kuryshev.model.dao.InMemoryDao;
import net.kuryshev.model.entity.Location;
import net.kuryshev.model.entity.User;
import net.kuryshev.model.entity.Visit;
import org.omg.PortableServer.THREAD_POLICY_ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.BindException;

@Controller
public class EntityController {

    private InMemoryDao dao;

    @Autowired
    public EntityController(InMemoryDao dao) {
        this.dao = dao;
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public String getUser(@PathVariable("id") String pathId, Model model) {
        int id = convertId(pathId);
        String json = dao.getJsonUser(id);
        if (json == null) throw new ResourceNotFoundException();
        model.addAttribute("json", json);
        return "response";
    }

    @RequestMapping(value = "/users/{id}/visits", method = RequestMethod.GET)
    public String getUserVisits(@PathVariable("id") String pathId,
                                @RequestParam(value = "fromDate", defaultValue = Long.MIN_VALUE + "") long fromDate,
                                @RequestParam(value = "toDate", defaultValue = Long.MAX_VALUE + "") long toDate,
                                @RequestParam(value = "country", defaultValue = "") String country,
                                @RequestParam(value = "toDistance", defaultValue = Integer.MAX_VALUE + "") int toDistance,
                                Model model) {
        int id = convertId(pathId);
        String json = dao.getJsonUserVisits(id, fromDate, toDate, country, toDistance);
        if (json == null) throw new ResourceNotFoundException();
        model.addAttribute("json", json);
        return "response";
    }

    private int convertId(String pathId) {
        try {
            return Integer.parseInt(pathId);
        } catch (NumberFormatException e) {
            throw new ResourceNotFoundException();
        }
    }


    @RequestMapping(value = "/locations/{id}/avg", method = RequestMethod.GET)
    public String getLocationsAvg(@PathVariable("id") String pathId,
                                  @RequestParam(value = "fromDate", defaultValue = Long.MIN_VALUE + "") long fromDate,
                                  @RequestParam(value = "toDate", defaultValue = Long.MAX_VALUE + "") long toDate,
                                  @RequestParam(value = "fromAge", defaultValue = Long.MIN_VALUE + "") long fromAge,
                                  @RequestParam(value = "toAge", defaultValue = Long.MAX_VALUE + "") long toAge,
                                  @RequestParam(value = "gender", defaultValue = "") String gender,
                                  Model model) {
        int id = convertId(pathId);
        checkGender(gender);
        String json = dao.getJsonLocationAvg(id, fromDate, toDate, fromAge, toAge, gender);
        if (json == null) throw new ResourceNotFoundException();
        model.addAttribute("json", json);
        return "response";
    }

    private void checkGender(String gender) {
        if (!gender.isEmpty() && !gender.equals("f") && !gender.equals("m")) throw new BadRequestException();
    }

    @RequestMapping(value = "/locations/{id}", method = RequestMethod.GET)
    public String getLocation(@PathVariable("id") String pathId, Model model) {
        int id = convertId(pathId);
        String json = dao.getJsonLocation(id);
        if (json == null) throw new ResourceNotFoundException();
        model.addAttribute("json", json);
        return "response";
    }

    @RequestMapping(value = "/visits/{id}", method = RequestMethod.GET)
    public String getVisit(@PathVariable("id") String pathId, Model model) {
        int id = convertId(pathId);
        String json = dao.getJsonVisit(id);
        if (json == null) throw new ResourceNotFoundException();
        model.addAttribute("json", json);
        return "response";
    }

    @RequestMapping(value = "/visits/{id}", method = RequestMethod.POST)
    public String postVisit(@PathVariable("id") String pathId, @RequestBody Visit visit, Model model) {
        int id = convertId(pathId);
        dao.updateVisit(id, visit);
        model.addAttribute("json", "{}");
        return "response";
    }

    @RequestMapping(value = "/locations/{id}", method = RequestMethod.POST)
    public String postLocation(@PathVariable("id") String pathId, @RequestBody Location location, Model model) {
        int id = convertId(pathId);
        dao.updateLocation(id, location);
        model.addAttribute("json", "{}");
        return "response";
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.POST)
    public String postUser(@PathVariable("id") String pathId, @RequestBody User user, Model model) {
        int id = convertId(pathId);
        dao.updateUser(id, user);
        model.addAttribute("json", "{}");
        return "response";
    }

    @RequestMapping(value = "/visits/new", method = RequestMethod.POST)
    public String postNewVisit(@RequestBody Visit visit, Model model) {
        dao.addVisit(visit);
        model.addAttribute("json", "{}");
        return "response";
    }

    @RequestMapping(value = "/locations/new", method = RequestMethod.POST)
    public String postNewLocation(@RequestBody Location location, Model model) {
        dao.addLocation(location);
        model.addAttribute("json", "{}");
        return "response";
    }

    @RequestMapping(value = "/users/new", method = RequestMethod.POST)
    public String postNewUser(@RequestBody User user, Model model) {
        dao.addUser(user);
        model.addAttribute("json", "{}");
        return "response";
    }


}
