using System;
using System.Drawing;
using System.Collections.Generic;

namespace WinUpdate
{
    class Frameset : List<Bitmap>, IDisposable
    {
        public Frameset(Bitmap b, int framecount)
        {
            if (!Bitmap.IsCanonicalPixelFormat(b.PixelFormat) || !Bitmap.IsAlphaPixelFormat(b.PixelFormat))
                throw new ApplicationException("The picture must be 32bit picture with alpha channel.");

            FrameWidth = b.Width / framecount;
            FrameHeight = b.Height;

            for (int i = 0; i < framecount; i++)
            {
                Bitmap bitmap = new Bitmap(FrameWidth, FrameHeight);
                using (Graphics g = Graphics.FromImage(bitmap))
                    g.DrawImage(b, new Rectangle(0, 0, FrameWidth, FrameHeight), new Rectangle(FrameWidth * i, 0, FrameWidth, FrameHeight), GraphicsUnit.Pixel);

                Add(bitmap);
            }
        }

        public int FrameWidth { get; private set; }

        public int FrameHeight { get; private set; }

        public void Dispose()
        {
            foreach (Bitmap f in this)
                f.Dispose();

            Clear();
        }
    }
}
