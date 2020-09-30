namespace UsersControl
{
    partial class ChangeAdminPasswordForm
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
            this.oldPasswordValue = new System.Windows.Forms.TextBox();
            this.newPassword = new System.Windows.Forms.TextBox();
            this.newPasswordConfirm = new System.Windows.Forms.TextBox();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.changeButton = new System.Windows.Forms.Button();
            this.errorMessage = new System.Windows.Forms.Label();
            this.passwordRequirements = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // oldPasswordValue
            // 
            this.oldPasswordValue.Location = new System.Drawing.Point(297, 60);
            this.oldPasswordValue.Name = "oldPasswordValue";
            this.oldPasswordValue.PasswordChar = '*';
            this.oldPasswordValue.Size = new System.Drawing.Size(182, 22);
            this.oldPasswordValue.TabIndex = 0;
            // 
            // newPassword
            // 
            this.newPassword.Location = new System.Drawing.Point(297, 149);
            this.newPassword.Name = "newPassword";
            this.newPassword.PasswordChar = '*';
            this.newPassword.Size = new System.Drawing.Size(182, 22);
            this.newPassword.TabIndex = 1;
            // 
            // newPasswordConfirm
            // 
            this.newPasswordConfirm.Location = new System.Drawing.Point(297, 240);
            this.newPasswordConfirm.Name = "newPasswordConfirm";
            this.newPasswordConfirm.Size = new System.Drawing.Size(182, 22);
            this.newPasswordConfirm.TabIndex = 2;
            this.newPasswordConfirm.PasswordChar = '*';
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(146, 64);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(129, 17);
            this.label1.TabIndex = 3;
            this.label1.Text = "Enter old password";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(146, 154);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(135, 17);
            this.label2.TabIndex = 4;
            this.label2.Text = "Enter new password";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(146, 245);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(149, 17);
            this.label3.TabIndex = 5;
            this.label3.Text = "Confirm new password";
            // 
            // changeButton
            // 
            this.changeButton.Location = new System.Drawing.Point(234, 298);
            this.changeButton.Name = "changeButton";
            this.changeButton.Size = new System.Drawing.Size(127, 40);
            this.changeButton.TabIndex = 6;
            this.changeButton.Text = "Confirm change";
            this.changeButton.UseVisualStyleBackColor = true;
            this.changeButton.Click += new System.EventHandler(this.ChangeButton_Click);
            // 
            // errorMessage
            // 
            this.errorMessage.AutoSize = true;
            this.errorMessage.ForeColor = System.Drawing.Color.Red;
            this.errorMessage.Location = new System.Drawing.Point(146, 20);
            this.errorMessage.Name = "errorMessage";
            this.errorMessage.Size = new System.Drawing.Size(0, 17);
            this.errorMessage.TabIndex = 7;
            // 
            // passwordRequirements
            // 
            this.passwordRequirements.AutoSize = true;
            this.passwordRequirements.Location = new System.Drawing.Point(44, 351);
            this.passwordRequirements.Name = "passwordRequirements";
            this.passwordRequirements.Size = new System.Drawing.Size(0, 17);
            this.passwordRequirements.TabIndex = 8;
            // 
            // ChangeAdminPasswordForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(570, 387);
            this.Controls.Add(this.passwordRequirements);
            this.Controls.Add(this.errorMessage);
            this.Controls.Add(this.changeButton);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.newPasswordConfirm);
            this.Controls.Add(this.newPassword);
            this.Controls.Add(this.oldPasswordValue);
            this.Name = "ChangeAdminPasswordForm";
            this.Text = "ChangeAdminPasswordForm";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        private void setPasswordRequirements()
        {
            if (User.isSpecialPasswordEnabled)
            {
                this.errorMessage.Text = "Enter the symbols, separating marks and again symbols.\nMinimal length is 8 symbols";
            }
            else
            {
                this.errorMessage.Text = "Enter the password with length at least of 8 symbols";
            }
        }

        #endregion

        private System.Windows.Forms.TextBox oldPasswordValue;
        private System.Windows.Forms.TextBox newPassword;
        private System.Windows.Forms.TextBox newPasswordConfirm;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Button changeButton;
        private System.Windows.Forms.Label errorMessage;
        private connection.DBConfig dbConfig;
        private System.Windows.Forms.Label passwordRequirements;
    }
}