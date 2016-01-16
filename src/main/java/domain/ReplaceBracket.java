package domain;

/**
 * Created by bruntha on 1/12/16.
 */
public class ReplaceBracket
{
    public static void main(String[] args)
    {
        String str=":-)(";
        String repStr=str.replaceAll("\\(","P");
        System.out.println(str +" is replcaed to  " +repStr);
    }
}