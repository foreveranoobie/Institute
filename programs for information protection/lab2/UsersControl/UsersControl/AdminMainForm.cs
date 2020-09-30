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
    public partial class AdminMainForm : Form
    {
        public AdminMainForm()
        {
            InitializeComponent();
        }

        private void ChangePassword_Click(object sender, EventArgs e)
        {
            ChangeAdminPasswordForm changeAdminPasswordForm = new ChangeAdminPasswordForm();
            changeAdminPasswordForm.setDBConfig(dbConfig);
            changeAdminPasswordForm.Show();
        }

        private void UsersList_Click(object sender, EventArgs e)
        {
            UsersListForm usersList = new UsersListForm(dbConfig);
            usersList.Show();
        }

        public void setDbConfig(connection.DBConfig dbConfig)
        {
            this.dbConfig = dbConfig;
        }
    }
}
