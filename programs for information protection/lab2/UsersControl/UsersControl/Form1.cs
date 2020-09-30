using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using UsersControl.connection;
using Npgsql;

namespace UsersControl
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
            InitializeDatabaseConnection();
            setPasswordNumbers();
        }

        private void Button1_Click(object sender, EventArgs e)
        {
            String username = usernameValue.Text;
            String password = passwordValue.Text;
            if (LoginUser(username, password))
            {
                if (User.isBlocked)
                {
                    BlockedUserMessage blockedUserMessage = new BlockedUserMessage();
                    blockedUserMessage.Closed += (s, args) => this.Close();
                    blockedUserMessage.Show();
                    return;
                }
                if (User.login.Equals("ADMIN"))
                {
                    if (User.password == null || User.password.Equals(""))
                    {
                        this.Hide();
                        RegisterUserPasswordForm registerUserPasswordForm = new RegisterUserPasswordForm();
                        registerUserPasswordForm.setDBConfig(dbConfig);
                        registerUserPasswordForm.Closed += (s, args) => this.Show();
                        registerUserPasswordForm.Show();
                    }
                    else
                    {
                        this.Hide();
                        AdminMainForm adminForm = new AdminMainForm();
                        adminForm.setDbConfig(dbConfig);
                        adminForm.Closed += (s, args) => this.Close();
                        adminForm.Show();
                    }
                }
                else
                {
                    if (User.password == null || User.password.Equals(""))
                    {
                        this.Hide();
                        RegisterUserPasswordForm registerUserPasswordForm = new RegisterUserPasswordForm();
                        registerUserPasswordForm.setDBConfig(dbConfig);
                        registerUserPasswordForm.Closed += (s, args) => this.Show();
                        registerUserPasswordForm.Show();
                    }
                    else
                    {
                        this.Hide();
                        UserMainForm userForm = new UserMainForm();
                        userForm.setDBConfig(dbConfig);
                        userForm.Closed += (s, args) => this.Close();
                        userForm.Show();
                    }
                }
            }
            else
            {
                if (checkIfUserExists(username))
                {
                    if (++loginAttempts < 3)
                    {
                        errorMessage.Text = "Incorrect password. Current attempt is " + loginAttempts + "/3";
                        setPasswordNumbers();
                        return;
                    }
                    this.Close();
                }
                else
                {
                    errorMessage.Text = "User with current login doesn't exist";
                }
            }
        }

        private void setPasswordNumbers()
        {
            Random random = new Random();
            firstPasswordNumber = random.Next(0, 7);
            secondPasswordNumber = random.Next(0, 7);
            while (firstPasswordNumber == secondPasswordNumber)
            {
                secondPasswordNumber = random.Next(0, 7);
            }
            passwordRequirement.Text = String.Format("Enter the {0} and {1} symbols from your password", firstPasswordNumber + 1, secondPasswordNumber + 1);
        }

        private Boolean LoginUser(String username, String password)
        {
            String query = "select id, login, password, \"isPasswordLimited\", " +
                "(select case when exists(select * from blocked_users where blocked_users.id = users.id) " +
                "THEN CAST(1 AS BOOLEAN) ELSE CAST(0 AS BOOLEAN) END) as isBlocked from users where login='" + username + "'";
            NpgsqlCommand getCommand = new NpgsqlCommand(query);
            getCommand.Connection = dbConfig.getConnection();
            NpgsqlDataReader result = getCommand.ExecuteReader();
            bool isNotEmpty = result.Read() && passwordSymbolsAreCorrect(result.GetString(2), password);
            if (isNotEmpty)
            {
                User.id = result.GetInt32(0);
                User.login = result.GetString(1);
                User.password = result.GetString(2);
                User.isSpecialPasswordEnabled = result.GetBoolean(3);
                User.isBlocked = result.GetBoolean(4);
            }
            result.Close();
            getCommand.Dispose();
            return isNotEmpty;
        }

        private bool passwordSymbolsAreCorrect(String password, String enteredPassword)
        {
            if (password.Length >= 8 && enteredPassword.Length > 2)
            {
                return (password.ElementAt(firstPasswordNumber).Equals(enteredPassword.ElementAt(0)) && password.ElementAt(secondPasswordNumber).Equals(enteredPassword.ElementAt(1)));
            }
            return true;
        }

        private bool checkIfUserExists(String username)
        {
            String query = "SELECT * from users where login='" + username + "'";
            NpgsqlCommand getCommand = new NpgsqlCommand(query);
            getCommand.Connection = dbConfig.getConnection();
            NpgsqlDataReader result = getCommand.ExecuteReader();
            bool isNotEmpty = result.Read();
            result.Close();
            getCommand.Dispose();
            return isNotEmpty;
        }

        private void InitializeDatabaseConnection()
        {
            dbConfig = new DBConfig();
        }

        private void Form1_Close(object sender, EventArgs e)
        {
            dbConfig.getConnection().Close();
        }
    }
}
