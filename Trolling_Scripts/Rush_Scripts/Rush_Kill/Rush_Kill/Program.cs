using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Rush_Kill
{
    class Program
    {
        static void Main(string[] args)
        {
            //Process[] procs = Process.GetProcessesByName("RUSH*");

            //Console.WriteLine(" [+] KILLING: " + procs);

            //foreach (Process p in procs) {
            //    Console.WriteLine(" [+] KILLING: " + p);
            //    p.Kill();
            //}
            

            Process.Start("taskkill", "/F /IM RUSH*");
            //System.Threading.Thread.Sleep(5000);

        }
    }
}
