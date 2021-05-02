package kvz.zsu.control.controllers;

import kvz.zsu.control.models.*;
import kvz.zsu.control.models.Object;
import kvz.zsu.control.services.*;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class RestAjaxController {

    private final ScopeService scopeService;
    private final ThingService thingService;
    private final UnitService unitService;
    private final ObjectService objectService;

    @GetMapping("/getScopsData")
    public Map<String, String> getScopsName() {
        Map<String, String> scopeData = new HashMap<>();

        for (var item : scopeService.findAll()) {
            List<Object> objects = new ArrayList<>();
            for (var i : item.getTypeList()){
                objects.addAll(i.getObjectList());
            }
            List<Thing> things = new ArrayList<>();
            for (var i : objects){
                things.addAll(i.getThingList());
            }
            Integer countOutput = 0;
            for (var i : things){
                countOutput += i.getGeneralHave();
            }

            scopeData.put(item.getScope(), countOutput.toString());
        }

        return scopeData;
    }

    @GetMapping("/getPercent")
    public List<String> getPercentInfo(){
        Integer intUk = thingService.percentUnitListByObjectList(unitService.findAll(), objectService.findAll());
        Integer intNoyUk = 100 - intUk;
        List<String> listOutput = new ArrayList<>();
        listOutput.add(intUk.toString());
        listOutput.add(intNoyUk.toString());
        return listOutput;
    }

    @GetMapping("/getNullUnitValue")
    public Map<String, Integer> getNullUnitValue() {
        Map<String, Integer> map = new HashMap<>();
        List<Unit> unitList = unitService.findAll().stream().filter(x -> x.getParentUnit() == null).collect(Collectors.toList());
        List<Object> objectList = objectService.findAll();

        for (var item : unitList){
            map.put(item.getNameUnit(), thingService.percentUnitByObjectList(item, objectList));
        }
        return map;
    }



}
