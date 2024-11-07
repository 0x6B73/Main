using System;
using System.Windows.Forms;

namespace WinUpdate
{
    internal partial class SysTrayFishForm : WinUpdate.WinUpdateForm
    {
        public SysTrayFishForm()
        {
            InitializeComponent();
        }

        public SysTrayFishForm(FishAnimation animation)
            : base(animation)
        {
            InitializeComponent();
        }

        private void exitToolStripMenuItem_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        private void addFishToolStripMenuItem_Click(object sender, EventArgs e)
        {
            Program.TheProgram.AddFish();
        }

        private void removeFishToolStripMenuItem_Click(object sender, EventArgs e)
        {
            Program.TheProgram.RemoveFish();
        }

        private void hideToolStripMenuItem_Click(object sender, EventArgs e)
        {
            Program.TheProgram.ShowHide();
            if (hideToolStripMenuItem.Text == "Hide")
                hideToolStripMenuItem.Text = "Show";
            else
                hideToolStripMenuItem.Text = "Hide";
        }
    }
}
