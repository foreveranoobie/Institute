using UsersControl.connection;

namespace UsersControl
{
    partial class AdminMainForm
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
            this.changePassword = new System.Windows.Forms.Button();
            this.usersList = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // changePassword
            // 
            this.changePassword.Location = new System.Drawing.Point(64, 56);
            this.changePassword.Name = "changePassword";
            this.changePassword.Size = new System.Drawing.Size(174, 56);
            this.changePassword.TabIndex = 0;
            this.changePassword.Text = "Change password";
            this.changePassword.UseVisualStyleBackColor = true;
            this.changePassword.Click += new System.EventHandler(this.ChangePassword_Click);
            // 
            // usersList
            // 
            this.usersList.Location = new System.Drawing.Point(64, 184);
            this.usersList.Name = "usersList";
            this.usersList.Size = new System.Drawing.Size(174, 56);
            this.usersList.TabIndex = 1;
            this.usersList.Text = "Users list";
            this.usersList.UseVisualStyleBackColor = true;
            this.usersList.Click += new System.EventHandler(this.UsersList_Click);
            // 
            // AdminMainForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(296, 340);
            this.Controls.Add(this.usersList);
            this.Controls.Add(this.changePassword);
            this.Name = "AdminMainForm";
            this.Text = "AdminMainForm";
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button changePassword;
        private System.Windows.Forms.Button usersList;
        private DBConfig dbConfig;
    }
}