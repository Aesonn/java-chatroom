package packets;

import java.io.Serializable;

public class packet_newRoom implements Serializable {

    private String name;
    private String Description;
    private String Question;
    private String Answer;

    public packet_newRoom(String name, String des, String ques, String ans) {
        this.name = name;
        this.Description = des;
        this.Question = ques;
        this.Answer = ans;
    }

    public packet_newRoom(String name, String des) {
        this.name = name;
        this.Description = des;
        this.Question = null;
        this.Answer = null;
    }
        
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the Description
     */
    public String getDescription() {
        return Description;
    }

    /**
     * @return the Question
     */
    public String getQuestion() {
        return Question;
    }

    /**
     * @return the Answer
     */
    public String getAnswer() {
        return Answer;
    }
}