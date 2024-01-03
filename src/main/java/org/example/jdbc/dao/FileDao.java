package org.example.jdbc.dao;

import org.example.hw6.dao.CrudRepository;
import org.example.jdbc.constant.SqlConstants;
import org.example.jdbc.model.dto.enums.Type;
import org.example.jdbc.model.entity.File;
import org.example.jdbc.util.PostgreSqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileDao implements CrudRepository<File, Long> {

    private static File buildFile(final ResultSet resultSet) throws SQLException {
        return File.builder()
            .id(resultSet.getLong("id"))
            .name(resultSet.getString("name"))
            .type(Type.valueOf(resultSet.getString("type")))
            .url(resultSet.getString("url"))
            .extensionId(resultSet.getLong("extension"))
            .build();
    }

    @Override
    public List<File> findAll() {
        Connection connection = PostgreSqlHelper.getConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SqlConstants.SELECT_FROM_FILES);

            List<File> fileList = new ArrayList<>();
            while (resultSet.next()) {
                fileList.add(buildFile(resultSet));
            }

            return fileList;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<File> findById(final Long id) {
        Connection connection = PostgreSqlHelper.getConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SqlConstants.getStringSelectFileById(id));
            resultSet.next();
            return Optional.ofNullable(buildFile(resultSet));
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public File save(final File user) {
        Connection connection = PostgreSqlHelper.getConnection();
        try (PreparedStatement statement =
                 connection.prepareStatement(SqlConstants.INSERT_INTO_FILES_NAME_TYPE_URL_EXTENSION_VALUES_S_S_S_D,
                     Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getType().name());
            statement.setString(3, user.getUrl());
            statement.setLong(4, user.getExtensionId());
            statement.executeUpdate();
            connection.commit();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                user.setId(keys.getLong(1));
                return user;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void deleteById(final Long id) {
        Connection connection = PostgreSqlHelper.getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.execute(SqlConstants.getStringDeleteFileById(id));
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public File update(final File user) {
        Connection connection = PostgreSqlHelper.getConnection();
        try (Statement statement = connection.createStatement()) {
            if (statement.execute(SqlConstants.getStringUpdateFileByIdWithValues(user.getId(),
                user.getName(),
                user.getType(),
                user.getUrl(),
                user.getExtensionId()))) {
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
