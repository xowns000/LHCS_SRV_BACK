package kr.co.hkcloud.palette3.core.security.crypto;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import kr.co.hkcloud.palette3.core.security.scpdb.agent.util.ScpdbAgentUtilsMybatis;
import kr.co.hkcloud.palette3.core.support.PaletteSpringContextSupport;

public class DamoPartialDecryptionCipherTypeHandler  implements TypeHandler<String>
{

    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException
    {
        try
        {
            ScpdbAgentUtilsMybatis scpdbAgentUtilsMybatis = PaletteSpringContextSupport.getBean(ScpdbAgentUtilsMybatis.class);
            parameter = scpdbAgentUtilsMybatis.partialDecryption(parameter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ps.setString(i, parameter);
    }

    @Override
    public String getResult(ResultSet rs, String columnName) throws SQLException
    {
        String value = rs.getString(columnName);
        try
        {
            ScpdbAgentUtilsMybatis scpdbAgentUtilsMybatis = PaletteSpringContextSupport.getBean(ScpdbAgentUtilsMybatis.class);
            value = scpdbAgentUtilsMybatis.partialDecryption(value);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public String getResult(ResultSet rs, int columnIndex) throws SQLException
    {
        String value = rs.getString(columnIndex);
        try
        {
            ScpdbAgentUtilsMybatis scpdbAgentUtilsMybatis = PaletteSpringContextSupport.getBean(ScpdbAgentUtilsMybatis.class);
            value = scpdbAgentUtilsMybatis.partialDecryption(value);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public String getResult(CallableStatement cs, int columnIndex) throws SQLException
    {
        String value = cs.getString(columnIndex);
        try
        {
            ScpdbAgentUtilsMybatis scpdbAgentUtilsMybatis = PaletteSpringContextSupport.getBean(ScpdbAgentUtilsMybatis.class);
            value = scpdbAgentUtilsMybatis.partialDecryption(value);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return value;
    }

}
