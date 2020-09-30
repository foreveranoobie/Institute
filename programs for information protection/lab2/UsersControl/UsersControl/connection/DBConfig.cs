using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Npgsql;

namespace UsersControl.connection
{
    public class DBConfig
    {
        private NpgsqlConnection connection;

        public DBConfig()
        {
            initializeDatabaseConnection();
        }

        private void initializeDatabaseConnection()
        {
            string connString = string.Format("Server={0};Username={1};Database={2};Port={3};Password={4};SSLMode=Prefer",
                "localhost", "postgres", "control", "5432", "admin");
            connection = new NpgsqlConnection(connString);
            connection.Open();
        }

        public NpgsqlConnection getConnection()
        {
            return connection;
        }
    }
}
