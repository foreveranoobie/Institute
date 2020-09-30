using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Management;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using Microsoft.Win32;
using static System.Management.ManagementObjectCollection;

namespace InfoCollector
{
    class Program
    {
        static void Main(string[] args)
        {
            RegistryKey registry = null;
            try
            {
                registry = Registry.CurrentUser.OpenSubKey("Software", true);
                registry = registry.CreateSubKey("Storozhuk");
                registry.SetValue("username", SystemInformation.UserName);
                registry.SetValue("computer_name", SystemInformation.ComputerName);
                registry.SetValue("root_cat", Environment.SystemDirectory);
                registry.SetValue("mouse_buttons", SystemInformation.MouseButtons);
                registry.SetValue("monitor_height", SystemInformation.PrimaryMonitorSize.Height);
                ManagementObjectSearcher moSearcher = new ManagementObjectSearcher("SELECT * FROM Win32_DiskDrive");
                ManagementObjectEnumerator enumerator = moSearcher.Get().GetEnumerator();
                enumerator.MoveNext();
                registry.SetValue("hdd_serial_number", enumerator.Current["SerialNumber"].ToString());
                int hddCount = 1;
                while (enumerator.MoveNext())
                {
                    hddCount++;
                }
                registry.SetValue("hdd_count", hddCount);
            }
            catch (Exception ex)
            {
                MessageBox.Show("exc");
            }
            finally
            {
                registry.Close();
            }
        }
    }
}
