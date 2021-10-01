import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		Connection con = null;
		Scanner sc = new Scanner(System.in);
		System.out.println("*** 명령어 목록 ***");
		System.out.println("1. 데이터조회");
		System.out.println("2. 데이터추가");
		System.out.println("3. 데이터수정");
		System.out.println("4. 데이터삭제");

		boolean run = true;
		String id;
		String name;
		String a_type ;
		String a_year ;
		String debut ;
		int num;
		try {
			// check org.sqlite.JDBC
			Class.forName("org.sqlite.JDBC");

			// connects database file
			String dbFile = "myfirst.db";
			con = DriverManager.getConnection("jdbc:sqlite:" + dbFile);

			while (run) {
				System.out.print("\nInput the command > ");
				int command = sc.nextInt();
				sc.nextLine();

				
				switch (command) {
				case 1:
					// READ the data
					System.out.println("\n*** 데이터 조회 ***");
					Statement stat1 = con.createStatement();
					String sql1 = "select * from g_artists";
					ResultSet rs1 = stat1.executeQuery(sql1);

					while (rs1.next()) {
						id = rs1.getString("id");
						name = rs1.getString("name");
						a_type = rs1.getString("a_type");
						System.out.println(id + " " + name + " " + a_type);
					}
					stat1.close();
					break;
					
				case 2:
					// CREATE the data

					System.out.println("\n*** 새 데이터 추가 ***");
					String sql2 = "insert into g_artists (name, a_type, a_year, debut, regdate)"
							+ " values (?,?,?,?,?)";
					PreparedStatement pstmt = con.prepareStatement(sql2);
					// read the input
					System.out.print("새로 추가할 가수의 이름을 입력하세요 > ");
					name = sc.next().trim();
					sc.nextLine();
					System.out.print("해당 가수의 활동유형을 입력하세요 > ");
					a_type = sc.nextLine();
					System.out.print("해당가수의 활동연대를 입력하세요 > ");
					a_year = sc.nextLine();
					System.out.print("해당가수의 데뷔년도를 입력하세요 > ");
					debut = sc.nextLine();
					// set the statement
					pstmt.setString(1, name);
					pstmt.setString(2, a_type);
					pstmt.setString(3, a_year);
					pstmt.setString(4, debut);
					pstmt.setString(5, "datetime('now', 'localtime')");
					int cnt2 = pstmt.executeUpdate();
					if (cnt2 > 0)
						System.out.println("새로운 데이터가 추가되었습니다!");
					else
						System.out.println("[Error] 데이터 추가 오류!");
					pstmt.close();
					break;
					
				case 3:
					// UPDATE the data
					System.out.println("\n*** 새 데이터 수정 ***");
					String sql3 = "update g_artists set name=?,regdate=? where name=?";
					PreparedStatement pstmt2 = con.prepareStatement(sql3);
					// read the input
					System.out.print("수정할 가수의 이름을 입력하세요 > ");
					name = sc.next();
					sc.nextLine();
					System.out.print("새로 입력할 가수의 이름을 입력하세 > ");
					String new_name = sc.next();
					sc.nextLine();

					pstmt2.setString(1, new_name);
					pstmt2.setString(2, "datetime('now', 'localtime')");
					pstmt2.setString(3, name);
					int cnt3 = pstmt2.executeUpdate();
					if (cnt3 > 0)
						System.out.println("데이터가 수정되었습니다!!");
					else
						System.out.println("[Error] 데이터수정 오류!!");

					pstmt2.close();
					break;
					
				case 4:
					// DELETE the data
					System.out.println("\n*** 새 데이터 삭제 ***");
					Statement stat4 = con.createStatement();
					System.out.print("삭제할 목록의 번호를 입력해주세요 > ");
					num = sc.nextInt();
					sc.nextLine();
					String sql4 = "delete from g_artists where id=" + num + " ;";
					int cnt4 = stat4.executeUpdate(sql4);
					if (cnt4 > 0)
						System.out.println("데이터가 삭제되었습니다!!");
					else
						System.out.println("[Error] 데이터삭제 오류!!");
					break;

				default:
					run = false;
					System.out.println("프로그램을 종료합니다!!\n");
					break;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
				}
			}
		}

	}

}
