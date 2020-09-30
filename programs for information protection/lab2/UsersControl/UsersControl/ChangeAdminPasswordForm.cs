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
using System.Text.RegularExpressions;

namespace UsersControl
{
    public partial class ChangeAdminPasswordForm : Form
    {
        public ChangeAdminPasswordForm()
        {
            InitializeComponent();
            setPasswordRequirements();
        }

        private void ChangeButton_Click(object sender, EventArgs e)
        {
            String realOldPassword = getRealOldPassword();
            if (realOldPassword.Equals(oldPasswordValue.Text))
            {
                if (newPassword.Text.Equals(newPasswordConfirm.Text))
                {
                    if (isCorrectFormat())
                    {
                        updatePassword(newPassword.Text);
                        errorMessage.Text = "";
                        this.Close();
                        return;
                    }
                    else
                    {
                        errorMessage.Text = "Incorrect password format!";
                    }
                }
                else
                {
                    errorMessage.Text = "New password and new confirmation password are different!";
                }
            }
            else
            {
                errorMessage.Text = "Your old password is incorrect";
            }
        }

        private bool isCorrectFormat()
        {
            String pattern;
            Match match;
            if (User.isSpecialPasswordEnabled)
            {
                pattern = @"[A-Za-z0-9]{4,}[;,:-]+[A-Za-z0-9]{4,}";
            }
            else
            {
                pattern = @"[A-Za-z0-9]{8,}";
            }
            match = Regex.Match(newPassword.Text, pattern);
            return match.Success;
        }

        private void updatePassword(string newPassword)
        {
            String query = String.Format("UPDATE users SET password='{0}' where login='{1}'", newPassword, User.login);
            NpgsqlCommand updateCommand = new NpgsqlCommand(query);
            updateCommand.Connection = dbConfig.getConnection();
            updateCommand.ExecuteNonQuery();
            updateCommand.Dispose();
        }

        private String getRealOldPassword()
        {
            String query = "SELECT * FROM users where login='" + User.login + "'";
            NpgsqlCommand getCommand = new NpgsqlCommand(query);
            getCommand.Connection = dbConfig.getConnection();
            NpgsqlDataReader result = getCommand.ExecuteReader();
            String oldPassword = null;
            while (result.Read())
            {
                oldPassword = result.GetString(2);
            }
            result.Close();
            getCommand.Dispose();
            return oldPassword;
        }

        public void setDBConfig(DBConfig dbConfig)
        {
            this.dbConfig = dbConfig;
        }
    }
}
