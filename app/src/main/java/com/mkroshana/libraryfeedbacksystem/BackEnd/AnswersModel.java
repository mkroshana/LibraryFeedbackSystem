package com.mkroshana.libraryfeedbacksystem.BackEnd;

public class AnswersModel
{
    private String Ans01, Ans02;

    /**
     * Feedback answers model for displaying the charts
     * @param ans01
     * @param ans02
     */
    public AnswersModel(String ans01, String ans02)
    {
        Ans01 = ans01;
        Ans02 = ans02;
    }

    public String getAns01()
    {
        return Ans01;
    }

    public String getAns02()
    {
        return Ans02;
    }
}
