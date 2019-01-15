package optiheat.storage;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import optiheat.storage.model.*;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MockData
{
    public static List<User> users;

    public static void populateMockDataFromFiles(String userId) throws Exception
    {
        users = new ArrayList<>();
        Unit unitForJSON = (Unit) readFromJSONFile("src/test/java/optiheat/storage/samples/unitSample.json", Unit.class);
        Unit unit = createUnit(unitForJSON);


        List<Iteration> iterations = convertExcelToJSON("src/test/java/optiheat/storage/samples/UnitData.xlsx", unit);
        int i = 0;
        for (Iteration iteration : iterations)
        {
            /*Unit newUnit = new Unit();
            newUnit.id = unit.id;
            newUnit.name = unit.name;
            iteration.unit = newUnit;
            writeToJSONFile(iteration, "samples/iteration_" + i + ".json");
            i++;*/
            addIterationToUnit(iteration, unit);
        }
        User user = new User();
        user.id = userId;
        if (user.units == null)
            user.units = new HashSet<>();
        user.units.add(unit);
        unit.user = user;
        users.add(user);
    }

    public static User getUser(String userId)
    {
        return users.stream().filter(x -> x.id.equals(userId)).findFirst().get();
    }

    public User createNewUser(String userId)
    {
        User user = new User();
        user.id = userId;
        users.add(user);
        return user;
    }

    public static Unit createUnit(Unit unit){
        for (Room room : unit.rooms){
            room.unit = unit;
        }
        return unit;
    }

    public static void addIterationToUnit(Iteration iteration, Unit unit){
        Iteration newIteration = new Iteration();
        newIteration.datetime = iteration.datetime;
        newIteration.sequence = iteration.sequence;

        if (iteration.roomMeasurements != null)
        {
            for (RoomMeasurement roomMeasurement : iteration.roomMeasurements)
            {
                Room correspondingRoom = unit.rooms.stream().filter(x -> x.id.equals(roomMeasurement.room.id)).findFirst().get();
                if (correspondingRoom.roomMeasurements == null)
                    correspondingRoom.roomMeasurements = new HashSet<>();
                correspondingRoom.roomMeasurements.add(roomMeasurement);
                roomMeasurement.room = correspondingRoom;
                if (newIteration.roomMeasurements == null)
                    newIteration.roomMeasurements = new HashSet<>();
                newIteration.roomMeasurements.add(roomMeasurement);
                roomMeasurement.iteration = newIteration;
            }
        }

        if (iteration.roomSettings != null)
        {
            for (RoomSetting roomSetting : iteration.roomSettings)
            {
                Room correspondingRoom = unit.rooms.stream().filter(x -> x.id.equals(roomSetting.room.id)).findFirst().get();
                if (correspondingRoom.roomSettings == null)
                    correspondingRoom.roomSettings = new HashSet<>();
                correspondingRoom.roomSettings.add(roomSetting);
                roomSetting.room = correspondingRoom;
                if (newIteration.roomSettings == null)
                    newIteration.roomSettings = new HashSet<>();
                newIteration.roomSettings.add(roomSetting);
                roomSetting.iteration = newIteration;
            }
        }

        if (iteration.unitSetting != null)
        {
            if (unit.unitSettings == null)
                unit.unitSettings = new HashSet<>();
            unit.unitSettings.add(iteration.unitSetting);
            iteration.unitSetting.unit = unit;

            newIteration.unitSetting = iteration.unitSetting;
            iteration.unitSetting.iteration = newIteration;
        }

        if (iteration.unitMeasurement != null)
        {
            if (unit.unitMeasurements == null)
                unit.unitMeasurements = new HashSet<>();
            unit.unitMeasurements.add(iteration.unitMeasurement);
            iteration.unitMeasurement.unit = unit;

            newIteration.unitMeasurement = iteration.unitMeasurement;
            iteration.unitMeasurement.iteration = newIteration;
        }

        if (unit.iterations == null)
            unit.iterations = new HashSet<>();
        unit.iterations.add(newIteration);
        newIteration.unit = unit;
    }

    private static Object readFromJSONFile(String path, Class cl)
    {
        try
        {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(path));
            return gson.fromJson(reader, cl);
        }
        catch (Exception ex)
        {
            int a = 1;
        }
        return null;
    }


    public static List<Iteration> convertExcelToJSON(String path, Unit unit) throws IOException, ParseException
    {
        List<Iteration> iterations = new ArrayList<Iteration>();
        Workbook workbook = WorkbookFactory.create(new File(path));
        for (Sheet sheet : workbook)
        {
            List<String> columnNames = new ArrayList<String>();
            for (Row row : sheet)
            {
                if (row.getRowNum() == 0)
                {
                    for (Cell cell : row)
                    {
                        String value = cell.getRichStringCellValue().toString();
                        columnNames.add(value);
                    }
                }
                else
                {
                    Iteration iteration = null;
                    RoomSetting rs = null;
                    UnitMeasurement um = null;
                    UnitSetting us = null;
                    for (Cell cell : row)
                    {
                        int colIndex = cell.getColumnIndex();
                        String columnName = columnNames.get(colIndex);
                        if (columnName.equals("datum"))
                        {
                            String dateTime = cell.getRichStringCellValue().toString();
                            Date dt = new SimpleDateFormat("dd.MM.yyyy hh:mm").parse(dateTime);
                            if (sheet.getSheetName().equals("Measurements"))
                            {
                                iteration = new Iteration();
                                iteration.datetime = dt;
                                iteration.roomMeasurements = new HashSet<>();
                                iterations.add(iteration);
                            }
                            else if (sheet.getSheetName().equals("Settings"))
                            {
                                Optional<Iteration> iterOpt = iterations.stream().filter(x -> x.datetime.equals(dt)).findFirst();
                                if (iterOpt.isPresent())
                                {
                                    iteration = iterOpt.get();
                                }
                                else
                                {
                                    iteration = new Iteration();
                                    iteration.datetime = dt;
                                    iterations.add(iteration);
                                }
                            }
                        }
                        else if (columnName.equals("tzun"))
                        {
                            um = new UnitMeasurement();
                            um.t_Out = cell.getNumericCellValue();
                            iteration.unitMeasurement = um;
                        }
                        else if (columnName.equals("tgretja"))
                        {
                            um.t_Heat = cell.getNumericCellValue();
                        }
                        else if (columnName.contains("dnevna soba") || columnName.contains("spalnica") || columnName.contains("otroska soba") || columnName.contains("kopalnica"))
                        {
                            Room correspondingRoom = unit.rooms.stream().filter(x -> columnName.contains(x.name)).findFirst().get();
                            Room newRoom = new Room();
                            newRoom.id = correspondingRoom.id;
                            newRoom.name = correspondingRoom.name;
                            if (sheet.getSheetName().equals("Measurements"))
                            {
                                RoomMeasurement rm = new RoomMeasurement();
                                rm.room = newRoom;
                                rm.t = cell.getNumericCellValue();
                                iteration.roomMeasurements.add(rm);
                            }
                            else if (sheet.getSheetName().equals("Settings"))
                            {
                                if (iteration.roomSettings == null)
                                    iteration.roomSettings = new HashSet<>();
                                if (columnName.contains("setpoint"))
                                {
                                    rs = new RoomSetting();
                                    rs.room = newRoom;
                                    rs.t_Setpoint = cell.getNumericCellValue();
                                    iteration.roomSettings.add(rs);
                                }
                                else if (columnName.contains("valve"))
                                {
                                    rs.valveLevel = cell.getNumericCellValue();
                                }
                            }
                        }
                        else if (columnName.equals("t_min"))
                        {
                            us = new UnitSetting();
                            us.t_min = cell.getNumericCellValue();
                            iteration.unitSetting = us;

                        }
                        else if (columnName.equals("t_max"))
                        {
                            us.t_max = cell.getNumericCellValue();
                        }
                    }
                    //iterations.add(iteration);
                }
            }
        }
        return iterations;
    }
}
