using UsersControl.connection;
using Npgsql;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace UsersControl
{
    public partial class UsersListForm : Form
    {
        public UsersListForm()
        {
            InitializeComponent();
        }

        public UsersListForm(DBConfig dbConfig)
        {
            InitializeComponent();
            SetDbConfig(dbConfig);
            getUsers();
        }

        public void SetDbConfig(DBConfig dbConfig)
        {
            this.dbConfig = dbConfig;
        }

        private void getUsers()
        {
            String query = "select id, login, password, \"isPasswordLimited\", " +
                "(select case when exists(select * from blocked_users where blocked_users.id = users.id) " +
                "THEN CAST(1 AS BOOLEAN) ELSE CAST(0 AS BOOLEAN) END) as isBlocked from users where login!='ADMIN'";
            NpgsqlCommand getCommand = new NpgsqlCommand(query);
            getCommand.Connection = dbConfig.getConnection();
            NpgsqlDataReader result = getCommand.ExecuteReader();
            int currentRow = 0;
            while (result.Read())
            {
                dataGridView1.Rows.Add();
                dataGridView1.Rows[currentRow].Cells[0].Value = result.GetInt32(0);
                dataGridView1.Rows[currentRow].Cells[1].Value = result.GetString(1);
                try
                {
                    dataGridView1.Rows[currentRow].Cells[2].Value = result.GetString(2);
                }
                catch (InvalidCastException ex)
                {
                    dataGridView1.Rows[currentRow].Cells[2].Value = "";
                }
                dataGridView1.Rows[currentRow].Cells[3].Value = result.GetBoolean(3);
                dataGridView1.Rows[currentRow].Cells[4].Value = result.GetBoolean(4);
                currentRow++;
            }
            result.Close();
        }

        private void DataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            // if (dataGridView1.Rows[e.RowIndex].Cells[0].Value != null)
            //    {
            switch (e.ColumnIndex)
            {
                case 1:
                    updateUsername(e);
                    break;
                case 2:
                    break;
                case 3:
                    setLimitedPassword(e);
                    break;
                case 4:
                    if ((bool)dataGridView1.Rows[e.RowIndex].Cells[4].Value)
                    {
                        blockUser(e);
                    }
                    else
                    {
                        unblockUser(e);
                    };
                    break;
                default:
                    break;
                    //    }
            }
        }

        private void updateUsername(DataGridViewCellEventArgs e)
        {
            String query = String.Format("update users set login='{0}' where id = {1}", dataGridView1.Rows[e.RowIndex].Cells[e.ColumnIndex].Value, dataGridView1.Rows[e.RowIndex].Cells[0].Value);
            NpgsqlCommand updateCommand = new NpgsqlCommand(query);
            updateCommand.Connection = dbConfig.getConnection();
            updateCommand.ExecuteNonQuery();
        }

        private void setLimitedPassword(DataGridViewCellEventArgs e)
        {
            String query = String.Format("update users set \"isPasswordLimited\"={0} where id={1}", dataGridView1.Rows[e.RowIndex].Cells[e.ColumnIndex].Value, dataGridView1.Rows[e.RowIndex].Cells[0].Value);
            NpgsqlCommand updateCommand = new NpgsqlCommand(query);
            updateCommand.Connection = dbConfig.getConnection();
            updateCommand.ExecuteNonQuery();
        }

        private void blockUser(DataGridViewCellEventArgs e)
        {
            String query = "INSERT INTO blocked_users (id) values(" + dataGridView1.Rows[e.RowIndex].Cells[0].Value + ")";
            NpgsqlCommand updateCommand = new NpgsqlCommand(query);
            updateCommand.Connection = dbConfig.getConnection();
            updateCommand.ExecuteNonQuery();
        }

        private void unblockUser(DataGridViewCellEventArgs e)
        {
            String query = "DELETE FROM blocked_users WHERE id=" + dataGridView1.Rows[e.RowIndex].Cells[0].Value;
            NpgsqlCommand updateCommand = new NpgsqlCommand(query);
            updateCommand.Connection = dbConfig.getConnection();
            updateCommand.ExecuteNonQuery();
        }

        private void AddNewUserButton_Click(object sender, EventArgs e)
        {
            if (!(userLogin.Text == null || userLogin.Text.Equals("")))
            {
                String query = String.Format("INSERT INTO users (login, \"isPasswordLimited\") VALUES ('{0}', {1})", userLogin.Text, enableSpecialPassword.Checked);
                NpgsqlCommand insertCommand = new NpgsqlCommand(query);
                insertCommand.Connection = dbConfig.getConnection();
                try
                {
                    insertCommand.ExecuteNonQuery();
                    dataGridView1.Rows.Clear();
                    getUsers();
                }
                catch (Npgsql.PostgresException ex)
                {
                    new UserLoginExists().Show();
                }
                finally
                {
                    insertCommand.Dispose();
                }
            }
        }
    }
}
