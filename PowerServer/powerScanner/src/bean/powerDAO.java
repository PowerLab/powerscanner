package bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import util.DBMgr;

public class powerDAO {

	DBMgr dbm;
	Connection con;
	Statement stmt;
	PreparedStatement pstmt;
	ResultSet rs;

	public powerDAO() {
		dbm = DBMgr.getInstance();
	}

	public Vector<powerDTO> getpower() {
		Vector<powerDTO> list = new Vector<powerDTO>();

		con = dbm.getConnection();

		String sql = "select * from power order by id";

		try {
			// ������ ���̽��� �ִ� �����͵��� �����ͼ� ����Ʈ�� �߰��� (result.jsp�� ���)
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				powerDTO dto = new powerDTO();

				dto.setId(rs.getInt(1));
				dto.setPackagename(rs.getString(2));
				dto.setTotal(rs.getInt(3));
				dto.setLed(rs.getInt(4));
				dto.setCpu(rs.getInt(5));
				dto.setWifi(rs.getInt(6));
				dto.setThreeg(rs.getInt(7));
				dto.setGps(rs.getInt(8));
				dto.setAudio(rs.getInt(9));
				dto.setTime(rs.getInt(10));

				list.add(dto);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// �׻� ó���� ������ close����� ��.
			dbm.close(con, pstmt, rs);
		}
		return list;
	}

	public boolean inputPower(powerDTO dto) {
		boolean b = false;

		con = dbm.getConnection();

		String sql = "insert into power (packagename,total,led,cpu,wifi,threeg,gps,audio,time) "
				+ "values (\""
				+ dto.getPackagename()
				+ "\","
				+ dto.getTotal()
				+ ","
				+ dto.getLed()
				+ ","
				+ dto.getCpu()
				+ ","
				+ dto.getWifi()
				+ ","
				+ dto.getThreeg()
				+ ","
				+ dto.getGps()
				+ ","
				+ dto.getAudio() + "," + dto.getTime() + ")";
		try {
			// ������ ���� �����͸� ���� ����
			stmt = con.createStatement();

			int a = stmt.executeUpdate(sql);
			if (a > 0)
				b = true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// �׻� ó���� ������ close����� ��.
			dbm.close(con, stmt);
		}
		return b;
	}
}
