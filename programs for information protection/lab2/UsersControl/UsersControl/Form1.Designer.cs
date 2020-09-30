using Npgsql;
using UsersControl.connection;

namespace UsersControl
{
    partial class Form1
    {
        /// <summary>
        /// Обязательная переменная конструктора.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Освободить все используемые ресурсы.
        /// </summary>
        /// <param name="disposing">истинно, если управляемый ресурс должен быть удален; иначе ложно.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Код, автоматически созданный конструктором форм Windows

        /// <summary>
        /// Требуемый метод для поддержки конструктора — не изменяйте 
        /// содержимое этого метода с помощью редактора кода.
        /// </summary>
        private void InitializeComponent()
        {
            System.Windows.Forms.Label label2;
            this.button1 = new System.Windows.Forms.Button();
            this.usernameValue = new System.Windows.Forms.TextBox();
            this.passwordValue = new System.Windows.Forms.TextBox();
            this.label1 = new System.Windows.Forms.Label();
            this.errorMessage = new System.Windows.Forms.Label();
            this.passwordRequirement = new System.Windows.Forms.Label();
            label2 = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // label2
            // 
            label2.AutoSize = true;
            label2.Enabled = false;
            label2.Location = new System.Drawing.Point(71, 131);
            label2.Name = "label2";
            label2.Size = new System.Drawing.Size(69, 17);
            label2.TabIndex = 4;
            label2.Text = "Password";
            label2.TextAlign = System.Drawing.ContentAlignment.BottomRight;
            // 
            // button1
            // 
            this.button1.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F);
            this.button1.Location = new System.Drawing.Point(193, 208);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(119, 45);
            this.button1.TabIndex = 0;
            this.button1.Text = "Login";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.Button1_Click);
            // 
            // usernameValue
            // 
            this.usernameValue.Location = new System.Drawing.Point(162, 55);
            this.usernameValue.Name = "usernameValue";
            this.usernameValue.Size = new System.Drawing.Size(196, 22);
            this.usernameValue.TabIndex = 1;
            // 
            // passwordValue
            // 
            this.passwordValue.Location = new System.Drawing.Point(162, 126);
            this.passwordValue.Name = "passwordValue";
            this.passwordValue.PasswordChar = '*';
            this.passwordValue.Size = new System.Drawing.Size(196, 22);
            this.passwordValue.TabIndex = 2;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(71, 60);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(73, 17);
            this.label1.TabIndex = 3;
            this.label1.Text = "Username";
            // 
            // errorMessage
            // 
            this.errorMessage.AutoSize = true;
            this.errorMessage.ForeColor = System.Drawing.Color.Red;
            this.errorMessage.Location = new System.Drawing.Point(97, 293);
            this.errorMessage.Name = "errorMessage";
            this.errorMessage.Size = new System.Drawing.Size(0, 17);
            this.errorMessage.TabIndex = 5;
            // 
            // passwordRequirement
            // 
            this.passwordRequirement.AutoSize = true;
            this.passwordRequirement.Location = new System.Drawing.Point(110, 172);
            this.passwordRequirement.Name = "passwordRequirement";
            this.passwordRequirement.Size = new System.Drawing.Size(0, 17);
            this.passwordRequirement.TabIndex = 6;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(495, 364);
            this.Controls.Add(this.passwordRequirement);
            this.Controls.Add(this.errorMessage);
            this.Controls.Add(label2);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.passwordValue);
            this.Controls.Add(this.usernameValue);
            this.Controls.Add(this.button1);
            this.Name = "Form1";
            this.Text = "Login";
            this.Closed += new System.EventHandler(this.Form1_Close);
            this.ResumeLayout(false);
            this.PerformLayout();

        }
        #endregion

        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.TextBox usernameValue;
        private System.Windows.Forms.TextBox passwordValue;
        private System.Windows.Forms.Label label1;
        private DBConfig dbConfig;
        private int loginAttempts;
        private System.Windows.Forms.Label errorMessage;
        private int firstPasswordNumber;
        private int secondPasswordNumber;
        private System.Windows.Forms.Label passwordRequirement;
    }
}

