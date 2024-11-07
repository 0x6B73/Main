//##########################################################################
//¡ï¡ï¡ï¡ï¡ï¡ï¡ï           http://www.cnpopsoft.com           ¡ï¡ï¡ï¡ï¡ï¡ï¡ï
//¡ï¡ï          VB & C# source code and articles for free !!!           ¡ï¡ï
//¡ï¡ï¡ï¡ï¡ï¡ï¡ï                Davidwu                       ¡ï¡ï¡ï¡ï¡ï¡ï¡ï
//##########################################################################

using System;
using System.Drawing;
using System.Windows.Forms;
using System.Diagnostics;

namespace WinUpdate
{
    partial class WinUpdateForm : Form
    {
        private int m_count;
        private int m_maxCount = 50;

        private float m_stepX = 2f;
        private float m_stepY;
        private float m_left;
        private float m_top;

        private bool m_mouseDown;
        private bool m_speedMode;
        private int m_currentFrameIndex;

        private Point m_oldPoint = new Point(0, 0);
        private FishAnimation m_fishAnimation;

        private static Random _random = new Random(unchecked((int)(DateTime.Now.Ticks)));

        public WinUpdateForm()
        {
            InitializeComponent();
        }

        public WinUpdateForm(FishAnimation animation)
        {
            SetStyle(ControlStyles.AllPaintingInWmPaint | ControlStyles.UserPaint | ControlStyles.OptimizedDoubleBuffer, true);
            InitializeComponent();

            m_fishAnimation = animation;

            m_left = m_fishAnimation.Tank.Left - m_fishAnimation.Frames.FrameWidth;
            m_top = (float)0;
            //m_top = m_fishAnimation.Tank.Height * (float)_random.NextDouble();
        }

        private void timerSpeed_Tick(object sender, EventArgs e)
        {
            AnimateFish();
        }

        protected override void OnMouseClick(MouseEventArgs e)
        {
            SwitchDirections();

            base.OnMouseClick(e);
        }

        protected override void OnDoubleClick(EventArgs e)
        {
            //WinUpdate.Program.TheProgram.AddFish();
            //WinUpdate.Program.TheProgram.AddFish();
            //Close();


            base.OnDoubleClick(e);
        }

        protected override void OnMouseDown(MouseEventArgs e)
        {
            m_oldPoint = e.Location;
            m_mouseDown = true;

            base.OnMouseDown(e);
        }

        protected override void OnMouseUp(MouseEventArgs e)
        {
            if (e.Button == MouseButtons.Right)
            {
                //Close();
                //WinUpdate.Program.TheProgram.AddFish();
                //WinUpdate.Program.TheProgram.AddFish();
            }
            else
            {
                m_count = 0;
                m_maxCount = _random.Next(70) + 40;

                m_speedMode = true;
                m_mouseDown = false;

                timerSpeed.Interval = _random.Next(20) + 2;
                timerSpeed.Enabled = true;
            }

            base.OnMouseUp(e);
        }

        protected override void OnMouseMove(MouseEventArgs e)
        {
            if (m_mouseDown)
            {
                Left += (e.X - m_oldPoint.X);
                Top += (e.Y - m_oldPoint.Y);
                m_left = Left;
                m_top = Top;

                FixLeftTop();
            }

            base.OnMouseMove(e);
        }

        protected override CreateParams CreateParams
        {
            get
            {
                CreateParams cParms = base.CreateParams;
                cParms.ExStyle |= Win32.WS_EX_LAYERED;

                return cParms;
            }
        }

        private void AnimateFish()
        {
            if (!m_mouseDown)
            {
                m_count++;
                if (m_count > m_maxCount)
                {
                    m_maxCount = _random.Next(70) + 30;

                    if (m_speedMode)
                    {
                        m_speedMode = false;
                        timerSpeed.Enabled = false;
                    }

                    m_count = 0;
                    m_stepX = (float)_random.NextDouble() * 3f + 10.5f;
                    m_stepY = ((float)_random.NextDouble() - 0.5f) * 0.1f;
                }

                m_left = m_left + (m_fishAnimation.ToRight ? 1 : -1) * m_stepX;
                m_top = m_top + m_stepY;

                FixLeftTop();

                Left = (int)m_left;
                Top = (int)m_top;

                // on one chance in the tank width switch directions
                if (_random.Next(m_fishAnimation.Tank.Width) == 1)
                    SwitchDirections();
            }

            if (++m_currentFrameIndex >= m_fishAnimation.Frames.Count)
                m_currentFrameIndex = 0;

            if (m_fishAnimation.Frames.Count > 0)
                SetBits(m_fishAnimation[m_currentFrameIndex]);
        }

        public void UpdateFish()
        {
            if (Visible == false)
                Show();

            if (timerSpeed.Enabled == false)
                AnimateFish();
        }

        private void SwitchDirections()
        {
            m_fishAnimation.ToRight = !m_fishAnimation.ToRight;

            m_currentFrameIndex = 0;
            m_count = 0;
        }

        private void FixLeftTop()
        {
            if ((m_fishAnimation.ToRight && m_left > m_fishAnimation.Tank.Right) ||
                (!m_fishAnimation.ToRight && m_left < m_fishAnimation.Tank.Left - m_fishAnimation.Frames.FrameWidth))
                SwitchDirections();

            if (m_top < -m_fishAnimation.Frames.FrameHeight)
            {
                m_stepY = 1f;
                m_count = 0;
            }
            else if (m_top > m_fishAnimation.Tank.Height)
            {
                m_stepY = -1f;
                m_count = 0;
            }
        }

        private void SetBits(Bitmap bitmap)
        {
            Debug.Assert(!Disposing && !IsDisposed);

            IntPtr oldBits = IntPtr.Zero;
            IntPtr screenDC = Win32.GetDC(IntPtr.Zero);
            IntPtr hBitmap = IntPtr.Zero;
            IntPtr memDc = Win32.CreateCompatibleDC(screenDC);

            try
            {
                hBitmap = bitmap.GetHbitmap(Color.FromArgb(0));
                oldBits = Win32.SelectObject(memDc, hBitmap);

                Point topLoc = Location;
                Point srcLoc = new Point(0, 0);
                Size bitMapSize = bitmap.Size;

                Win32.BLENDFUNCTION blendFunc;

                blendFunc.BlendOp = Win32.AC_SRC_OVER;
                blendFunc.BlendFlags = 0;
                blendFunc.SourceConstantAlpha = 255;
                blendFunc.AlphaFormat = Win32.AC_SRC_ALPHA;

                Win32.UpdateLayeredWindow(Handle, screenDC, ref topLoc, ref bitMapSize, memDc, ref srcLoc, 0, ref blendFunc, Win32.ULW_ALPHA);
            }
            finally
            {
                if (hBitmap != IntPtr.Zero)
                {
                    Win32.SelectObject(memDc, oldBits);
                    Win32.DeleteObject(hBitmap);
                }
                Win32.ReleaseDC(IntPtr.Zero, screenDC);
                Win32.DeleteDC(memDc);
            }
        }
    }
}