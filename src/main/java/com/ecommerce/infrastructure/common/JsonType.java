package com.ecommerce.infrastructure.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;

public class JsonType implements UserType<Map<String, String>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public int getSqlType() {
        return Types.VARCHAR;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<Map<String, String>> returnedClass() {
        return (Class<Map<String, String>>) (Class<?>) Map.class;
    }

    @Override
    public boolean equals(Map<String, String> x, Map<String, String> y) {
        return x.equals(y);
    }

    @Override
    public int hashCode(Map<String, String> x) {
        return x.hashCode();
    }

    @Override
    public Map<String, String> nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        String json = rs.getString(position);
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, returnedClass());
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert String to Map", e);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Map<String, String> value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (value == null) {
            st.setNull(index, Types.VARCHAR);
        } else {
            try {
                st.setString(index, objectMapper.writeValueAsString(value));
            } catch (Exception e) {
                throw new RuntimeException("Failed to convert Map to String", e);
            }
        }
    }

    @Override
    public Map<String, String> deepCopy(Map<String, String> value) {
        return value;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Map<String, String> value) {
        return (Serializable) deepCopy(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, String> assemble(Serializable cached, Object owner) {
        return deepCopy((Map<String, String>) cached);
    }
}
