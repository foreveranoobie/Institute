namespace UsersControl
{
    partial class RegisterUserPasswordForm
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
            this.password = new System.Windows.Forms.TextBox();
            this.passwordConfirm = new System.Windows.Forms.TextBox();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.setPasswordButton = new System.Windows.Forms.Button();
            this.passwordRequirements = new System.Windows.Forms.Label();
            this.errorLabel = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // password
            // 
            this.password.Location = new System.Drawing.Point(216, 86);
            this.password.Name = "password";
            this.password.Size = new System.Drawing.Size(184, 22);
            this.password.TabIndex = 0;
            this.password.PasswordChar = '*';
            // 
            // passwordConfirm
            // 
            this.passwordConfirm.Location = new System.Drawing.Point(216, 163);
            this.passwordConfirm.Name = "passwordConfirm";
            this.passwordConfirm.Size = new System.Drawing.Size(184, 22);
            this.passwordConfirm.TabIndex = 1;
            this.passwordConfirm.PasswordChar = '*';
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(62, 90);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(135, 17);
            this.label1.TabIndex = 2;
            this.label1.Text = "Enter new password";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(61, 166);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(149, 17);
            this.label2.TabIndex = 3;
            this.label2.Text = "Confirm new password";
            // 
            // setPasswordButton
            // 
            this.setPasswordButton.Location = new System.Drawing.Point(167, 230);
            this.setPasswordButton.Name = "setPasswordButton";
            this.setPasswordButton.Size = new System.Drawing.Size(130, 35);
            this.setPasswordButton.TabIndex = 4;
            this.setPasswordButton.Text = "Set password";
            this.setPasswordButton.UseVisualStyleBackColor = true;
            this.setPasswordButton.Click += new System.EventHandler(this.SetPasswordButton_Click);
            // 
            // passwordRequirements
            // 
            this.passwordRequirements.AutoSize = true;
            this.passwordRequirements.Location = new System.Drawing.Point(62, 34);
            this.passwordRequirements.Name = "passwordRequirements";
            this.passwordRequirements.Size = new System.Drawing.Size(0, 17);
            this.passwordRequirements.TabIndex = 5;
            // 
            // errorLabel
            // 
            this.errorLabel.AutoSize = true;
            this.errorLabel.ForeColor = System.Drawing.Color.Red;
            this.errorLabel.Location = new System.Drawing.Point(16, 291);
            this.errorLabel.Name = "errorLabel";
            this.errorLabel.Size = new System.Drawing.Size(0, 17);
            this.errorLabel.TabIndex = 6;
            // 
            // RegisterUserPasswordForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(593, 333);
            this.Controls.Add(this.errorLabel);
            this.Controls.Add(this.passwordRequirements);
            this.Controls.Add(this.setPasswordButton);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.passwordConfirm);
            this.Controls.Add(this.password);
            this.Name = "RegisterUserPasswordForm";
            this.Text = "RegisterUserPasswordForm";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.TextBox password;
        private System.Windows.Forms.TextBox passwordConfirm;
        public System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Button setPasswordButton;
        private System.Windows.Forms.Label passwordRequirements;
        private System.Windows.Forms.Label errorLabel;
        private connection.DBConfig dbConfig;
    }
}