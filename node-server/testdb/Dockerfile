FROM mysql:latest

# Create Database
# RUN	mkdir /usr/sql
# RUN	chmod 644 /usr/sql

# ADD ["testdb.sql", "/usr/sql/testdb.sql"]
ENV MYSQL_DATABASE test
ENV MYSQL_ALLOW_EMPTY_PASSWORD yes
COPY testdb.sql /docker-entrypoint-initdb.d/testdb.sql
RUN /etc/init.d/mysql start & sleep 10

# EXPOSE 3306

# RUN /etc/init.d/mysql start && \
# sleep 5 && \
# mysql -u root -e "CREATE DATABASE test" && \
# mysql -u root -D test < /usr/sql/testdb.sql && \
# sleep 5
