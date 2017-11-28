import java.io.*;
import java.util.*;

public class Cards{

    private static final String MAIN_FILE_NAME = "cards.txt";
    private static final String MAIN_DIR_NAME = "./java";
    private static final String MAIN_CASE_LINE=
            "\nВведите одну из команд: \n" +
            "1 - перемешать колоду\n" +
            "2 - раздать колоду\n" +
            "3 - собрать карты\n" +
            "0 - выход\n";
    private static final String[] CARDS_ARRAY= {
            "clubs ace",
            "clubs 2",
            "clubs 3",
            "clubs 4",
            "clubs 5",
            "clubs 6",
            "clubs 7",
            "clubs 8",
            "clubs 9",
            "clubs 10",
            "clubs jack",
            "clubs queen",
            "clubs king",
            "diamonds ace",
            "diamonds 2",
            "diamonds 3",
            "diamonds 4",
            "diamonds 5",
            "diamonds 6",
            "diamonds 7",
            "diamonds 8",
            "diamonds 9",
            "diamonds 10",
            "diamonds jack",
            "diamonds queen",
            "diamonds king",
            "hearts ace",
            "hearts 2",
            "hearts 3",
            "hearts 4",
            "hearts 5",
            "hearts 6",
            "hearts 7",
            "hearts 8",
            "hearts 9",
            "hearts 10",
            "hearts jack",
            "hearts queen",
            "hearts king",
            "spades ace",
            "spades 2",
            "spades 3",
            "spades 4",
            "spades 5",
            "spades 6",
            "spades 7",
            "spades 8",
            "spades 9",
            "spades 10",
            "spades jack",
            "spades queen",
            "spades king"};


    public static void main (String args[]) throws IOException {

        String caseName="1";
        Console con = System.console();
        deleteFromDir(MAIN_DIR_NAME);
        writeToFile(MAIN_FILE_NAME, CARDS_ARRAY);
        while(!Objects.equals(caseName, "0")) {
            caseName = con.readLine(MAIN_CASE_LINE);
            switch (caseName) {
                case "1":
                    shaffleCard(caseName);
                    break;
                case "2":
                    giveCards(caseName);
                    break;
                case "3":
                    returnCards(caseName);
                    break;
                case "0":
                    exitFrom(caseName);
                    break;
                default: {
                    System.out.println("Неверная команда+\n");
                }
            }
        }
    }


    private static void shaffleCard(String caseName) throws FileNotFoundException {
        System.out.println("Выполнена команда - "+caseName+"\n");
        List<String> cards = read(MAIN_FILE_NAME);
        Collections.shuffle(cards);
        writeToFile(MAIN_FILE_NAME, cards.toArray(new String[cards.size()]));
        System.out.println("Колода перетасована:");
        loadFromFile(MAIN_FILE_NAME);
        System.out.println("Количество карт - "+cards.size()+"\n");
    }

    private static void giveCards(String caseName) throws IOException {
        Console con = System.console();
        System.out.println("Выполнена команда - "+caseName+"\n");
        List <String> cards = read(MAIN_FILE_NAME);
        int gamersCount = Integer.parseInt(con.readLine("Количество игроков:"));
        int cardsCount = Integer.parseInt(con.readLine("Количество карт на руки:"));
        if(gamersCount*cardsCount>cards.size())
        {
            System.out.println("В колоде недостаточно карт! В колоде - "+cards.size()+". Попытка выдать на руки - "+gamersCount*cardsCount);
        }
        else {
            List <String> card= new ArrayList<String>();
            for(int i=0;i<gamersCount;i++){
                card.clear();
                for(int j=0;j<cardsCount;j++){
                    card.add(cards.get(0));
                    cards.remove(0);
                }
                writeToFile("Gamer"+i, card.toArray(new String[card.size()]));
                System.out.println("на руках у игрока Gamer"+i+" карты: "+read("Gamer"+i).toString());
            }
            writeToFile(MAIN_FILE_NAME, cards.toArray(new String[cards.size()]));
            System.out.println("\nОсталось карт в колоде:");
            loadFromFile(MAIN_FILE_NAME);
        }
    }

    private static void returnCards(String caseName) throws IOException {
        System.out.println("\nВыполнена команда - "+caseName+"\n");
        List <String> gamerFilesName = loadNamesFromDir(MAIN_DIR_NAME);
        List <String> cards = new ArrayList<String>();
        for(int i=0;i<gamerFilesName.size();i++){
            List <String> card = read(gamerFilesName.get(i));
            cards.addAll(card);
            card.clear();
            writeToFile(gamerFilesName.get(i), card.toArray(new String[card.size()]));
        }
        writeToFile(MAIN_FILE_NAME, cards.toArray(new String[cards.size()]));
        System.out.println("Карты собраны в колоду:");
        loadFromFile(MAIN_FILE_NAME);
        System.out.println("Количество карт - "+cards.size());
        deleteFromDir(MAIN_DIR_NAME);
    }

    private static void exitFrom(String caseName) throws IOException {
        System.out.println("Выполнена команда - "+caseName);
        deleteFromDir(MAIN_DIR_NAME);
    }

    private static void writeToFile(String fileName, String[] text) {
        File file = new File(MAIN_DIR_NAME,fileName);

        try {
            if(!file.exists()){
                file.createNewFile();
            }

            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                for(int i=0;i<text.length;i++)
                    out.println(text[i]);
            } finally {
                out.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List <String> read(String fileName) throws FileNotFoundException {
        File file = new File(MAIN_DIR_NAME,fileName);
        List <String> sb = new ArrayList<String>();

        exists(fileName);

        try {
            BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
            try {
                String s;
                while ((s = in.readLine()) != null) {
                    sb.add(s);
                }
            } finally {
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return sb;
    }



    private static void exists(String fileName) throws FileNotFoundException {
        File file = new File(MAIN_DIR_NAME,fileName);
        if (!file.exists()){
            throw new FileNotFoundException(file.getName());
        }
    }

    private static void delete(String nameFile) throws FileNotFoundException {
        exists(nameFile);
        new File(MAIN_DIR_NAME, nameFile).delete();
    }

    private static void loadFromFile(String fileName) throws FileNotFoundException {
        File file = new File(MAIN_DIR_NAME,fileName);

        exists(fileName);

        try {
            BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
            try {
                String s;
                while ((s = in.readLine()) != null) {
                    System.out.println(s);
                }
            } finally {
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadFromDir(String dirName) throws IOException {
        File dir = new File(dirName);
        if (dir.isDirectory()) {
            for(File item : dir.listFiles()){
                if(!item.isDirectory()){
                    System.out.println(item.getName());
                }
            }
        }

    }

    private static List<String> loadNamesFromDir(String dirName) throws IOException {
        File dir = new File(dirName);
        List<String> dirList = new ArrayList<String>();
        if (dir.isDirectory()) {
            for(File item : dir.listFiles()){
                if(!item.isDirectory()){
                    dirList.add(item.getName());
                }
            }
        }
        return dirList;
    }

    private static void deleteFromDir(String dirName) throws IOException {
        File dir = new File(dirName);
        if (dir.isDirectory()) {
            for(File item : dir.listFiles()){
                if(!item.isDirectory()&&!item.getName().equals(MAIN_FILE_NAME)){
                    delete(item.getName());
                }
            }
        }

    }
}

