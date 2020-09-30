namespace UsersControl
{
    partial class UserMainForm
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
            this.changePasswordButton = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // changePasswordButton
            // 
            this.changePasswordButton.Location = new System.Drawing.Point(96, 78);
            this.changePasswordButton.Name = "changePasswordButton";
            this.changePasswordButton.Size = new System.Drawing.Size(144, 52);
            this.changePasswordButton.TabIndex = 0;
            this.changePasswordButton.Text = "Change password";
            this.changePasswordButton.UseVisualStyleBackColor = true;
            this.changePasswordButton.Click += new System.EventHandler(this.ChangePasswordButton_Click);
            // 
            // UserMainForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(331, 267);
            this.Controls.Add(this.changePasswordButton);
            this.Name = "UserMainForm";
            this.Text = "UserMainForm";
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button changePasswordButton;
        private connection.DBConfig dbConfig;
    }
}