package cz.marianjanik.ekurz;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

public class Main {
    private static DecimalFormat myFormat = new DecimalFormat("#,### Kč");
    public static void main(String[] args) throws IOException, SQLException {


        Reader reader = Resources.getResourceAsReader("MyBatisConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        sqlSessionFactory.getConfiguration().addMapper(ItemMapper.class);

        try (SqlSession session = sqlSessionFactory.openSession()) {
            ItemMapper itemMapper = session.getMapper(ItemMapper.class);





            System.out.println("\n-----------------------------------------------------Výpis dle vybraného id:");
            Item item = itemMapper.loadItemById(3);
            System.out.println(item.getName());

            System.out.println("\n---------------------------Odstranění všech položek, které nejsou na skladě:");
            itemMapper.deleteAllOutOfStockItems(); //todo nefunguje

            System.out.println("\n---------------------------------------------------------Výpis SQL databáze:\n");
            List<Item> items = itemMapper.loadAllAvailableItems();
            System.out.println(getAllInfo(items));

            System.out.println("\n------------------------------------------------------------Uložení položky:");
            final int ID_WHAT_I_WANT2 = 3;
            Item item2 = itemMapper.loadItemById(ID_WHAT_I_WANT2);
            System.out.println(getInfo1(item2));
            itemMapper.saveItem(item);
            session.commit();

            items = itemMapper.loadAllAvailableItems(); //------------------Kontrolní výpis
            System.out.println(getAllInfo(items));

            System.out.println("\n-----------------------------------------------------------Aktualizace ceny:");
            final int ID_WHAT_I_WANT3 = 5; //todo nefunguje
            final BigDecimal price = BigDecimal.valueOf(500);
            itemMapper.updatePrice(5,BigDecimal.valueOf(500));

            items = itemMapper.loadAllAvailableItems(); //------------------Kontrolní výpis
            System.out.println(getAllInfo(items));

        }
    }
    private static String getInfo1(Item item){
        StringBuilder builder = new StringBuilder();
        builder.append(item.getId() + ", " + item.getPartNo() + ", " + item.getSerialNo() + ", "
                + item.getName() + ", " + item.getDescription() + ", " + item.getNumberInStock() + ", "
                + myFormat.format(item.getPrice()));
        return builder.toString();
    }
    private static String getAllInfo (List<Item> items) {
        final int MAX_LENGTH_ID = 5;
        final int MAX_LENGTH_NAME = 18;
        final int MAX_LENGTH_DESCRIPTION = 10;
        final int MAX_LENGTH_NUMBER = 7;
        final int MAX_LENGTH_PRICE = 12;
        StringBuilder builder = new StringBuilder();
        builder.append("\nid -- partNo -- serialNo -- name ----- description -- numberInStock -- price\n\n");
        for (Item item : items) {
            builder.append(item.getId() + " " + dashes(MAX_LENGTH_ID - item.getId().toString().length()) + " "
                    + item.getPartNo() + " ------ "
                    + item.getSerialNo() + " -- "
                    + item.getName() + " " + dashes(MAX_LENGTH_NAME - item.getName().length()) + " "
                    + item.getDescription() + " " + ((item.getDescription() == null) ?
                    dashes(MAX_LENGTH_DESCRIPTION - 4) : dashes(MAX_LENGTH_DESCRIPTION
                    - item.getDescription().length())) + " " + item.getNumberInStock() + " "
                    + dashes(MAX_LENGTH_NUMBER - item.getNumberInStock().toString().length())
                    + dashes(MAX_LENGTH_PRICE - myFormat.format(item.getPrice()).length()) + " "
                    + myFormat.format(item.getPrice()) + "\n");
        }
        return builder.toString();
    }

    private static String dashes(int number) {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < number; i++) {
            builder.append("-");
        }
        return builder.toString();
    }

}
