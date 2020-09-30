using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows.Forms;
using UsersControl.connection;
using Npgsql;

namespace UsersControl
{
    public partial class RegisterUserPasswordForm : Form
    {
        public RegisterUserPasswordForm()
        {
            InitializeComponent();
            setPasswordRequirements();
        }

        private void setPasswordRequirements()
        {
            if (User.isSpecialPasswordEnabled)
            {
                this.passwordRequirements.Text = "Enter the symbols, separating marks and again symbols.\nMinimal length is 8 symbols";
            }
            else
            {
                this.passwordRequirements.Text = "Enter the password with length at least of 8 symbols";
            }
        }

        private void SetPasswordButton_Click(object sender, EventArgs e)
        {
            if (areFieldsEquals())
            {
                if (isCorrectFormat())
                {
                    savePassword();
                    this.Close();
                    return;
                }
                errorLabel.Text = "Incorrect password format!";
                return;
            }
            errorLabel.Text = "Password and confirmation inputs are different!";
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
            match = Regex.Match(password.Text, pattern);
            return match.Success;
        }

        private void savePassword()
        {
            String query = String.Format("update users set password='{0}' where id={1} and login='{2}'", password.Text, User.id, User.login);
            NpgsqlCommand setCommand = new NpgsqlCommand(query);
            setCommand.Connection = dbConfig.getConnection();
            setCommand.ExecuteNonQuery();
        }

        private bool areFieldsEquals()
        {
            return password.Text.Equals(passwordConfirm.Text);
        }

        public void setDBConfig(DBConfig dbConfig)
        {
            this.dbConfig = dbConfig;
        }
    }
}
