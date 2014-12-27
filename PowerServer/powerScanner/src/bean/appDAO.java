package bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import util.DBMgr;

public class appDAO {
	DBMgr dbm;
	Connection con;
	PreparedStatement stmt;
	ResultSet rs;

	public appDAO() {
		dbm = DBMgr.getInstance();
	}

	public Vector<appDTO> getapp() {
		Vector<appDTO> list = new Vector<appDTO>();

		con = dbm.getConnection();

		String sql = "select * from app order by id";

		try {
			// ������ ���̽��� �ִ� �����͵��� �����ͼ� ����Ʈ�� �߰��� (result_app.jsp�� ���)
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				appDTO dto = new appDTO();

				dto.setId(rs.getInt(1));
				dto.setPackagename(rs.getString(2));
				dto.setAvg_power(rs.getDouble(3));

				list.add(dto);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// �׻� ó���� ������ close����� ��.
			dbm.close(con, stmt, rs);
		}
		return list;
	}
}
