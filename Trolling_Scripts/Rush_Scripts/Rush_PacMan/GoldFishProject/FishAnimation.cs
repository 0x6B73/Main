using System;
using System.Drawing;
using System.Diagnostics;

namespace WinUpdate
{
    class FishAnimation : IDisposable
    {
        public FishAnimation(Rectangle tank, FramesetPair pair)
        {
            ToRight = true;
            Tank = tank;

            Debug.Assert(pair != null);
            Frames = pair; 
        }

        public bool ToRight { get; set; }

        public FramesetPair Frames { get; private set; }

        public Rectangle Tank { get; private set; }
        
        public Bitmap this[int index]
        {
            get
            {
                if (ToRight)
                    return Frames.Item2[index];

                return Frames.Item1[index];
            }
        }

        public void Dispose()
        {
            if (Frames != null)
                Frames.Dispose();
        }            
    }
}
