package com.eastcom.shopping.dao;

import com.eastcom.shopping.entity.Sales;
import com.eastcom.shopping.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SalesDao {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet resultSet = null;

    /**
     * 当日销售情况
     * @return
     */
    public ArrayList<Sales> dailySales() {
        ArrayList<Sales> salesList = new ArrayList<>();
        connection = DBUtils.connection();
        String sql = "select gname,gprice,gnum, allSum from" +
                "goods, (select gid as salesid,sum(snum) as allSum from sales where sDate =curdate() group by gid) as sale" +
                "where gid = salesid";
        try {
            ps = connection.prepareStatement(sql);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String gName = resultSet.getString(1);
                double gPrice = resultSet.getDouble(2);
                int gNum = resultSet.getInt(3);
                int allSum = resultSet.getInt("allSum");
                Sales sales = new Sales(gName, gPrice, gNum, allSum);
                salesList.add(sales);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtils.closeConnect(ps, resultSet, connection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return salesList;
    }
}