import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Test {
    public Document setQuestion(String html) {
        Document doc = Jsoup.parse(html);//解析HTML字符串返回一个Document实现
//        doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
        String flag = doc.getElementsByTag("div").attr("data-random");
        Elements input = doc.select("input");
        if (flag.equals("0")){
            for (Element link:input){
                showInput(input, link);
            }
        }else {
            int size = input.size();
            random(size,input);
        }
        return doc;
    }

    /**
     * 判分
     * @param html 字符型原始页面
     * @param question 出题页面
     * @param answer 作答
     * @return
     */
    private List getScore(String html, Document question, String[] answer) {
        List score = new ArrayList();
        Document doc = Jsoup.parse(html);//解析HTML字符串返回一个Document实现
        List ids = new ArrayList<>();
        List<String> answers = new ArrayList<>();
        Elements input = doc.select("input");
        Elements inputQuestion = question.select("input");
        for (Element link:inputQuestion) {
            int id = Integer.parseInt(link.attr("id"));
            ids.add(id);
        }
        for (int i = 0;i < input.size();i++) {
            String tips = input.get(i).attr("data-text");
            if (ids.contains(i)){
                answers.add(tips);
            }
        }
        for (int i = 0;i < answers.size();i++){
            if (answer[i].equals(answers.get(i))){
                score.add(1);
            }else {
                score.add(0);
            }
        }
        return score;
    }

    /**
     * 出题随机方法
     * @param size 非作答题数
     * @param input
     * @return
     */
    public int random(int size,Elements input){
        int random = (int)(Math.random()*(size-1)+1);
        List list = new ArrayList();
        for (int i = 0;i < size;i++){
            list.add(i);
        }
        Collections.shuffle(list);
        list = list.subList(0,random);
        for (Element link:input){
            if (list.contains(input.indexOf(link))){
                String tips = link.attr("data-text");
                Element element = new Element("label").append(tips);
                link.replaceWith(element);
            }else {
                showInput(input, link);
            }
        }
        return random;
    }

    /**
     * 出题
     * @param input
     * @param link
     */
    private void showInput(Elements input, Element link) {
        String tips = link.attr("data-tips");
        if (tips.contains("|")) {
            tips = tips.substring(0, tips.indexOf("|"));
        }
        link.removeAttr("data-id");
        link.removeAttr("data-text");
        link.removeAttr("data-tips");
        link.attr("id", String.valueOf(input.indexOf(link)));
        link.attr("placeholder", tips);
    }

    public static void main(String[] args) {
        Test t = new Test();
        String html = t.readFromFile(new File("C:\\Users\\56596\\Desktop\\meta-2.html"));
        Document document = t.setQuestion(html);
        System.out.print(document+"\n");
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        String[] answer  = str.split(" ");
        List score = t.getScore(html,document,answer);
        System.out.print(score);
    }

    public String readFromFile(File src) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(
                    src));
            StringBuilder stringBuilder = new StringBuilder();
            String content;
            while((content = bufferedReader.readLine() )!=null){
                stringBuilder.append(content);
            }
            return stringBuilder.toString();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }
}
