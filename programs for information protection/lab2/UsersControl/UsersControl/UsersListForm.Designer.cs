using UsersControl.connection;

namespace UsersControl
{
    partial class UsersListForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.dataGridView1 = new System.Windows.Forms.DataGridView();
            this.id = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.login = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.password = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.isPasswordSpecial = new System.Windows.Forms.DataGridViewCheckBoxColumn();
            this.isBlockedUser = new System.Windows.Forms.DataGridViewCheckBoxColumn();
            this.label1 = new System.Windows.Forms.Label();
            this.userLogin = new System.Windows.Forms.TextBox();
            this.label2 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.enableSpecialPassword = new System.Windows.Forms.CheckBox();
            this.addNewUserButton = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView1)).BeginInit();
            this.SuspendLayout();
            // 
            // dataGridView1
            // 
            this.dataGridView1.AllowUserToOrderColumns = true;
            this.dataGridView1.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.dataGridView1.Columns.AddRange(new System.Windows.Forms.DataGridViewColumn[] {
            this.id,
            this.login,
            this.password,
            this.isPasswordSpecial,
            this.isBlockedUser});
            this.dataGridView1.Location = new System.Drawing.Point(62, 61);
            this.dataGridView1.Name = "dataGridView1";
            this.dataGridView1.RowTemplate.Height = 24;
            this.dataGridView1.Size = new System.Drawing.Size(855, 155);
            this.dataGridView1.TabIndex = 0;
            this.dataGridView1.CellEndEdit += new System.Windows.Forms.DataGridViewCellEventHandler(this.DataGridView1_CellContentClick);
            this.dataGridView1.AllowUserToAddRows = false;
            // 
            // id
            // 
            this.id.HeaderText = "id";
            this.id.Name = "id";
            this.id.ReadOnly = true;
            // 
            // login
            // 
            this.login.HeaderText = "Login";
            this.login.MinimumWidth = 100;
            this.login.Name = "login";
            this.login.Width = 120;
            // 
            // password
            // 
            this.password.HeaderText = "Password";
            this.password.MinimumWidth = 100;
            this.password.Name = "password";
            this.password.ReadOnly = true;
            this.password.Width = 120;
            // 
            // isPasswordSpecial
            // 
            this.isPasswordSpecial.HeaderText = "Enable Special Password";
            this.isPasswordSpecial.MinimumWidth = 100;
            this.isPasswordSpecial.Name = "isPasswordSpecial";
            this.isPasswordSpecial.Width = 180;
            // 
            // isBlockedUser
            // 
            this.isBlockedUser.HeaderText = "Block User";
            this.isBlockedUser.MinimumWidth = 100;
            this.isBlockedUser.Name = "isBlockedUser";
            this.isBlockedUser.Width = 120;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(59, 256);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(94, 17);
            this.label1.TabIndex = 1;
            this.label1.Text = "Add new user";
            // 
            // userLogin
            // 
            this.userLogin.Location = new System.Drawing.Point(111, 292);
            this.userLogin.Name = "userLogin";
            this.userLogin.Size = new System.Drawing.Size(158, 22);
            this.userLogin.TabIndex = 2;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(62, 297);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(43, 17);
            this.label2.TabIndex = 3;
            this.label2.Text = "Login";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(65, 342);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(164, 17);
            this.label3.TabIndex = 4;
            this.label3.Text = "Enable special password";
            // 
            // enableSpecialPassword
            // 
            this.enableSpecialPassword.AutoSize = true;
            this.enableSpecialPassword.Location = new System.Drawing.Point(235, 343);
            this.enableSpecialPassword.Name = "enableSpecialPassword";
            this.enableSpecialPassword.Size = new System.Drawing.Size(18, 17);
            this.enableSpecialPassword.TabIndex = 5;
            this.enableSpecialPassword.UseVisualStyleBackColor = true;
            // 
            // addNewUserButton
            // 
            this.addNewUserButton.Location = new System.Drawing.Point(378, 292);
            this.addNewUserButton.Name = "addNewUserButton";
            this.addNewUserButton.Size = new System.Drawing.Size(108, 36);
            this.addNewUserButton.TabIndex = 6;
            this.addNewUserButton.Text = "Add new user";
            this.addNewUserButton.UseVisualStyleBackColor = true;
            this.addNewUserButton.Click += new System.EventHandler(this.AddNewUserButton_Click);
            // 
            // UsersListForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1032, 432);
            this.Controls.Add(this.addNewUserButton);
            this.Controls.Add(this.enableSpecialPassword);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.userLogin);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.dataGridView1);
            this.Name = "UsersListForm";
            this.Text = "UsersListForm";
            ((System.ComponentModel.ISupportInitialize)(this.dataGridView1)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.DataGridView dataGridView1;
        private DBConfig dbConfig;
        private System.Windows.Forms.DataGridViewTextBoxColumn id;
        private System.Windows.Forms.DataGridViewTextBoxColumn login;
        private System.Windows.Forms.DataGridViewTextBoxColumn password;
        private System.Windows.Forms.DataGridViewCheckBoxColumn isPasswordSpecial;
        private System.Windows.Forms.DataGridViewCheckBoxColumn isBlockedUser;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.TextBox userLogin;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.CheckBox enableSpecialPassword;
        private System.Windows.Forms.Button addNewUserButton;
    }
}