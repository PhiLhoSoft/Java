import java.sql.*;

// From SQLiteJDBC <http://www.zentus.com/sqlitejdbc/>
public class SQLiteTest
{
  public static void main(String[] args) throws Exception
  {
    Class.forName("org.sqlite.JDBC");
    Connection conn = DriverManager.getConnection("jdbc:sqlite:SQLiteTest.db");
    Statement stat = conn.createStatement();
    stat.executeUpdate("drop table if exists manga;");
    stat.executeUpdate("create table manga (author, title);");
    PreparedStatement prep = conn.prepareStatement("insert into manga values (?, ?);");

    prep.setString(1, "Tsukasa Hōjō");
    prep.setString(2, "City Hunter");
    prep.addBatch();
    prep.setString(1, "Rumiko Takahashi");
    prep.setString(2, "Ranma ½");
    prep.addBatch();
    prep.setString(1, "Mitsuru Adachi");
    prep.setString(2, "Rough");
    prep.addBatch();

    conn.setAutoCommit(false);
    prep.executeBatch();
    conn.setAutoCommit(true);

    ResultSet rs = stat.executeQuery("select author, title from manga;");
    while (rs.next())
    {
      System.out.println("Mangaka = " + rs.getString("author"));
      System.out.println("Title of manga = " + rs.getString("title"));
    }
    rs.close();
    conn.close();
  }
}
