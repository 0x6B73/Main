//##########################################################################
//¡ï¡ï¡ï¡ï¡ï¡ï¡ï           http://www.cnpopsoft.com           ¡ï¡ï¡ï¡ï¡ï¡ï¡ï
//¡ï¡ï          VB & C# source code and articles for free !!!           ¡ï¡ï
//¡ï¡ï¡ï¡ï¡ï¡ï¡ï                Davidwu                       ¡ï¡ï¡ï¡ï¡ï¡ï¡ï
//##########################################################################

using System;
using System.IO;
using System.Drawing;
using System.Reflection;
using System.Diagnostics;
using System.Windows.Forms;
using System.Collections.Generic;

using WinUpdate.Properties;

namespace WinUpdate
{
    class Program : IDisposable
    {
        private Timer m_timer = new Timer();
        private IList<WinUpdateForm> m_fish = new List<WinUpdateForm>();
        private IList<FramesetPair> m_framesets = new List<FramesetPair>();
        private Random m_random = new Random(unchecked((int)(DateTime.Now.Ticks)));

        public Program()
        {
            LoadAnimations();   
        }

        private void Show(int rushCount)
        {
            int count = rushCount;
            if (Settings.Default.FishCount > 1)
                //count = Settings.Default.FishCount;
                count = 1;

            for (int i = 0; i < count; i++)
                CreateAndAddFish();

            m_timer.Interval = 50;
            m_timer.Enabled = true;
            m_timer.Tick += new EventHandler(timer_Tick);

            Application.Run(m_fish[0]);
        }

        public void Dispose()
        {
            m_timer.Dispose();
            m_fish.Clear();

            foreach (IDisposable d in m_framesets)
                d.Dispose();

            m_framesets.Clear();
        }

        

        /// <summary>
        /// Main Entry
        /// </summary>
        [STAThread]
        static void Main(String[] args)
        {
            if (args == null || args.Length == 0)
            {
                int rushCount = 1;
                using (_program = new Program())
                    _program.Show(rushCount);
            }
            else
            {
                int rushCount = System.Convert.ToInt32(args[0]);
                using (_program = new Program())
                    _program.Show(rushCount);

            }

    }

        private static Program _program;

        public static Program TheProgram
        {
            get
            {
                return _program;
            }
        }

        public static string[] OBJECT_PICKED { get; private set; }

        private void LoadAnimations()
        {
            m_framesets.Add(LoadFramesets("Fish"));
        }

        private static FramesetPair LoadFramesets(string color)
        {
            using (Stream left = Assembly.GetExecutingAssembly().GetManifestResourceStream(string.Format("FishTank.Resources.{0}.Matrix_Large_Black_Transparent.png", color)))
            using (Stream right = Assembly.GetExecutingAssembly().GetManifestResourceStream(string.Format("FishTank.Resources.{0}.Matrix_Large_Black_Transparent.png", color)))
            using (Bitmap leftBitmap = new Bitmap(left))
            using (Bitmap rightBitmap = new Bitmap(right))
                return new FramesetPair(new Frameset(leftBitmap, 8), new Frameset(rightBitmap, 8));
        }

        private void CreateAndAddFish()
        {
            Rectangle tank = new Rectangle();
            foreach (Screen screen in Screen.AllScreens)
                tank = Rectangle.Union(tank, screen.WorkingArea);

            FishAnimation animation = new FishAnimation(tank, GetFramesets());
            WinUpdateForm f = null;
            if (m_fish.Count > 0)
                f = new WinUpdateForm(animation);
            else
                f = new SysTrayFishForm(animation);

            f.Disposed += new EventHandler(f_Disposed);
            m_fish.Add(f);
        }

        private FramesetPair GetFramesets()
        {
            return m_framesets[m_random.Next(m_framesets.Count)];
        }

        public void AddFish()
        {
            CreateAndAddFish();

            Settings.Default.FishCount = m_fish.Count;
            Settings.Default.Save();
        }

        public void ShowHide()
        {
            m_timer.Enabled = !m_timer.Enabled;

            foreach (WinUpdateForm f in m_fish)
                f.Visible = m_timer.Enabled;
        }

        public void RemoveFish()
        {
            if (m_fish.Count >= 1)
            {
                m_fish[m_fish.Count - 1].Dispose();

                Settings.Default.FishCount = m_fish.Count;
                Settings.Default.Save();
            }
        }

        private void f_Disposed(object sender, EventArgs e)
        {
            WinUpdateForm f = (WinUpdateForm)sender;
            m_fish.Remove(f);
            f.Disposed -= new EventHandler(f_Disposed);
        }

        private void timer_Tick(object sender, EventArgs e)
        {
            foreach (WinUpdateForm f in m_fish)
                f.UpdateFish();
        }
    }
}