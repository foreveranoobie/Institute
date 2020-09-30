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
    public partial class UserMainForm : Form
    {
        public UserMainForm()
        {
            InitializeComponent();
        }

        private void ChangePasswordButton_Click(object sender, EventArgs e)
        {
            ChangeAdminPasswordForm changeAdminPasswordForm = new ChangeAdminPasswordForm();
            changeAdminPasswordForm.setDBConfig(dbConfig);
            changeAdminPasswordForm.Show();
        }

        public void setDBConfig(connection.DBConfig dbConfig)
        {
            this.dbConfig = dbConfig;
        }
    }
}
