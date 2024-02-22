using Microsoft.Win32;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace CodeNode
{
    /// <summary>
    /// Interaktionslogik für MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
        }

        private void OpenJavaFile(object sender, RoutedEventArgs e)
        {
            OpenFileDialog fileDialog = new OpenFileDialog();
            fileDialog.Multiselect = false;
            
            Nullable<bool> result = fileDialog.ShowDialog();

            // Get the selected file name and display in a TextBox 
            if (result == true)
            {
                OpenFile(fileDialog.FileName);
            }
        }

        void OpenFile(string fileName)
        {
            string javaCode = File.ReadAllText(fileName);

            string pattern = @"(\w+)\s+(\w+)\s*\(([^)]*)\)\s*\{|(\w+)\s+(\w+)\s*;|class\s+(\w+)";
            MatchCollection matches = Regex.Matches(javaCode, pattern);

            foreach (Match match in matches)
            {
                if (match.Groups[1].Success) // Method signature
                {
                    string returnType = match.Groups[1].Value;
                    string methodName = match.Groups[2].Value;
                    string parameters = match.Groups[3].Value;

                    // Split parameters by commas and trim whitespace
                    string[] parameterArray = parameters.Split(',').Select(p => p.Trim()).ToArray();

                    Console.WriteLine($"Method: {returnType} {methodName}({string.Join(", ", parameterArray)})");
                }
                else if (match.Groups[4].Success) // Variable declaration
                {
                    string variableType = match.Groups[4].Value;
                    string variableName = match.Groups[5].Value;
                    Console.WriteLine($"Variable: {variableType} {variableName}");
                }
                else if (match.Groups[6].Success) // Variable declaration
                {
                    string className = match.Groups[6].Value;
                    Console.WriteLine($"Class: {className} ");
                }
            }
        }

        private void CloseApplication(object sender, RoutedEventArgs e)
        {
            Application.Current.Shutdown(0);
        }
    }
}
