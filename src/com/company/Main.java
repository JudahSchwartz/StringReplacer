package com.company;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Replacement[] replacements = loadReplacementsFromProperties();
        Scanner scanner = new Scanner(System.in);
        for(Replacement rep: replacements){
            System.out.println("What value would you like to replace for " + rep.getName());
            rep.setValue(scanner.nextLine());
            System.out.println(rep);
        }
        String template = null;
        try {
            template = Files.readString(Paths.get("template.txt"), StandardCharsets.US_ASCII);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Replacement rep: replacements){
            template = template.replace(rep.getPattern(),rep.getValue());
        }
        System.out.println("finished template : \n" + template);
    }

    private static Replacement[] loadReplacementsFromProperties() {
        try {
            JSONObject json = getJsonFromFile();
            JSONArray replacements = json.getJSONArray("replacements");
            Replacement[] reps = new Replacement[replacements.length()];
            for(int i = 0; i < reps.length;i++)
            {
                reps[i] = new Replacement(replacements.getJSONObject(i));
            }
            return reps;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return new Replacement[0];
    }

    private static JSONObject getJsonFromFile() throws IOException {
        String content = Files.readString(Paths.get("properties.json"), StandardCharsets.US_ASCII);
        try {
            return new JSONObject(content);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
class Replacement{
    String name,pattern,value;

    public  Replacement(JSONObject jsonObject) {
        try {
            name = jsonObject.getString("name");
            pattern = jsonObject.getString("pattern");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String toString(){
        return "name: " + name + " pattern: " + pattern + " value: " + value;
    }
    public Replacement setValue(String value){
        this.value = value;
        return this;
    }
    public String getName(){
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getPattern() {
        return pattern;
    }
}
