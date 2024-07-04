package web.controllers;

import core.io.City;
import core.io.CityFromFile;
import core.io.ConfigDAO;
import core.io.SetUpUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.services.MainPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class MainPaigeController {
    private final MainPageService mainPageService;

    @GetMapping("/")
    public String mainPage(Model model) throws IOException {
        setAttribute(model);
        return "mainPage";
    }


    @PostMapping("/RUN")
    @ResponseBody
    public void go(@RequestParam("selectedMethod") String method, Model model) throws IOException {
        String errorMessage;
        ConfigDAO dao = new ConfigDAO();
        if (method.equals("GA")){
            if (!mainPageService.tspGaRestriction()){
                errorMessage = "It is impossible to solve with these restrictions, a minimum of " +
                        SetUpUtil.calcNecessaryTraveler() + " travelers is required";
                throw new RuntimeException(errorMessage);
            }
        } else if (method.equals("B&B")) {

            if (dao.getNumOfPoints() - 1 + dao.getNumOfTraveler() > 17){
                throw new RuntimeException("Too hard");
            }

            if (!mainPageService.tspBnbRestriction()){
                errorMessage = "It is impossible to solve with these restrictions, a minimum of " +
                        SetUpUtil.calcNecessaryTraveler() + " travelers is required";
                throw new RuntimeException(errorMessage);
            }
        }
        setAttribute(model);
    }

    @PostMapping("/GENERATE")
    public String generate(Model model) throws IOException {
        mainPageService.newTask();
        setAttribute(model);
        System.out.println("wadawdawd");
        return "redirect:/";
    }

    @PostMapping("/processSaveButtonConfigTSP")
    @ResponseBody
    public void receiveDataFromConfigTspPAge(@RequestParam(value = "numOfCities", defaultValue = "0") String numOfCities,
                            @RequestParam(value = "numOfTraveler", defaultValue = "0") String numOfTraveler,
                            @RequestParam(value = "checkboxForConstraints") boolean isCheckedConstraints,
                            @RequestParam(value = "maxLoad", defaultValue = "0") String maxLoad) throws IOException {
        ConfigDAO dao = new ConfigDAO();
        dao.setNumOfPoints(Integer.parseInt(numOfCities));
        dao.setLoadCapacityForTraveler(Integer.parseInt(maxLoad));
        dao.setNumOfTraveler(Integer.parseInt(numOfTraveler));
        dao.setRestrictionsNeeded(isCheckedConstraints);
    }

    @PostMapping("/processSaveButtonConfigGA")
    @ResponseBody
    public void receiveDataFromConfigGaPage(@RequestParam(value = "mutationRate", defaultValue = "0") String mutationRate,
                            @RequestParam(value = "populationSize", defaultValue = "0") String populationSize,
                            @RequestParam(value = "maxStagnation", defaultValue = "0") String maxStagnation,
                            @RequestParam(value = "tournamentSize", defaultValue = "0") String tournamentSize,
                            @RequestParam(value = "elitismCount", defaultValue = "0") String elitismCount,
                            @RequestParam(value = "numOfGeneration", defaultValue = "0") String numOfGeneration) throws IOException {
        ConfigDAO dao = new ConfigDAO();
        dao.setElitismCount(Integer.parseInt(elitismCount));
        dao.setMutationRate(Double.parseDouble(mutationRate));
        dao.setPopulationSize(Integer.parseInt(populationSize));
        dao.setTournamentSize(Integer.parseInt(tournamentSize));
        dao.setNumGenerations(Integer.parseInt(numOfGeneration));
        dao.setMaxStagnation(Integer.parseInt(maxStagnation));
    }

    @PostMapping("/processSaveButtonChangePathPaige")
    @ResponseBody
    public void receiveDataFromChangePathPage(@RequestParam(value = "newPath") String newPath) throws IOException {

        List<City> cities = mainPageService.getCities();
        List<City> changedRoute = new ArrayList<>();
        List<Integer> changedRouteAsIntegers = new ArrayList<>();
        String[] s = newPath.replace("[", "").replace("]", "").split(", ");
        for (String index : s) {
            changedRouteAsIntegers.add(Integer.parseInt(index));
        }
        for (Integer index : changedRouteAsIntegers){
            for (City c : cities){
                if (c.getCityIndex() == index){
                    changedRoute.add(c);
                }
            }
        }
        mainPageService.setChangedRoute(changedRoute);
        mainPageService.calculateNewCost(changedRouteAsIntegers);

        ConfigDAO dao = new ConfigDAO();
        changedRouteAsIntegers.removeIf(city -> city.equals(dao.getStartPoint()));
        Set<Integer> uniqueElements = new HashSet<>(changedRouteAsIntegers);
        if (uniqueElements.size() == changedRouteAsIntegers.size()) {
            if (changedRoute.size() == mainPageService.getRoute().size()){
                mainPageService.setChangedRoute(changedRoute);
                mainPageService.calculateNewCost(changedRouteAsIntegers);
            } else {
                throw new RuntimeException("The entered route does not contain all points");
            }
        } else {
            throw new RuntimeException("The entered route contains duplicates");
        }
    }

    @ExceptionHandler({RuntimeException.class})
    @ResponseBody
    public void handleRuntimeException(RuntimeException e, HttpServletResponse response) throws IOException {
        String errorMessage = e.getMessage();
        response.setContentType("text/plain; charset=UTF-8");
        response.getWriter().write(errorMessage);
    }


    @GetMapping("/get-dataConfigTSP")
    public ResponseEntity<String> getDataForConfigTspPaige() throws IOException {
        ConfigDAO dao = new ConfigDAO();
        String data = "{\"numOfCities\":\"" + dao.getNumOfPoints() +
                "\", \"numOfTraveler\":\"" + dao.getNumOfTraveler() +
                "\", \"useConstraints\":\"" + dao.isRestrictionsNeeded() +
                "\", \"methodName\":\"" + mainPageService.getMethodName() +
                "\", \"maxLoad\":\"" + dao.getLoadCapacityForTraveler() + "\"}";
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/get-dataConfigGA")
    public ResponseEntity<String> getDataForConfigGaPaige() throws IOException {
        ConfigDAO dao = new ConfigDAO();
        String data = "{\"mutationRate\":\"" + dao.getMutationRate() +
                "\", \"populationSize\":\"" + dao.getPopulationSize() +
                "\", \"maxStagnation\":\"" + dao.geMaxStagnation() +
                "\", \"tournamentSize\":\"" + dao.getTournamentSize() +
                "\", \"elitismCount\":\"" + dao.getElitismCount() +
                "\", \"numOfGeneration\":\"" + dao.getNumGenerations() + "\"}";
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/get-dataMainPage")
    public ResponseEntity<String> getDataForMainPaige() {
        String method = mainPageService.getMethodName();
        String checkedMethod = "";
        if (method.equals("Genetic algorithm") || method.equals("-")) checkedMethod = "GA";
        if (method.equals("Branch and Bound")) checkedMethod = "B&B";
        String data = "{\"checkedMethod\":\"" + checkedMethod + "\"}";
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/get-dataChangePaige")
    public ResponseEntity<String> getDataForChangePathPaige() {
        List<City> routeAsCity = mainPageService.getRoute();
        List<Integer> route = new ArrayList<>();
        for (City c : routeAsCity) {
            route.add(c.getCityIndex());
        }
        String data = "{\"path\":\"" + route + "\"}";
        return ResponseEntity.ok().body(data);
    }

    @PostMapping("/uploadFile")
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        mainPageService.setCities(CityFromFile.readFile(file));
        mainPageService.importTask();
        return "redirect:/";
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile() throws IOException {
        mainPageService.exportResult();
        Path path = Paths.get("report.txt");
        Resource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    private void setAttribute(Model model) throws IOException {
        ConfigDAO dao = new ConfigDAO();
        model.addAttribute("cities", mainPageService.getCities());
        model.addAttribute("route", mainPageService.getRoute());
        model.addAttribute("cost", mainPageService.getRouteCost());
        model.addAttribute("newCost", mainPageService.getNewRouteCost());
        model.addAttribute("method", mainPageService.getMethodName());
        model.addAttribute("numTraveler", dao.getNumOfTraveler());
        model.addAttribute("numCities", dao.getNumOfPoints());
        model.addAttribute("loadCapacity", dao.getLoadCapacityForTraveler());
        model.addAttribute("startCity", dao.getStartPoint());
        List<Integer> restrictions = new java.util.ArrayList<>(Arrays.stream(dao.getRestrictionsArray()).boxed().toList());
        restrictions.remove(0);
        restrictions.add(0);
        model.addAttribute("restrictions", restrictions);
        model.addAttribute("useRestrictions", dao.isRestrictionsNeeded());
        model.addAttribute("time", String.format("%,6.3f ms\n", mainPageService.getBestTime() / 1_000_000.0) );
        model.addAttribute("changedRoute", mainPageService.getChangedRoute());
        model.addAttribute("isRes", mainPageService.isRes());
    }
}
