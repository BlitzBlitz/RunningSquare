package sample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class GameData {

    private int topScore;
    private static GameData instance = new GameData();;

    private GameData(){
                    //making it singleton
    }

    public void loadData(){
        try (BufferedReader bufferedReader = new BufferedReader(
                new FileReader(System.getProperty("user.dir") + "\\data.txt"))){
            topScore = Integer.parseInt(bufferedReader.readLine());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveData(){
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getProperty("user.dir")+ "\\data.txt"))){
            bufferedWriter.write(topScore + "");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setTopScore(int topScore) {
        this.topScore = topScore;
    }

    public int getTopScore(){
        return topScore;
    }

    public static GameData getInstance(){
        return instance;
    }
}
