using System;
using System.Diagnostics;

namespace WinUpdate
{
    /// <summary>
    /// Holds two framesets 
    /// </summary>
    /// <remarks>One for the left swimming fish one for the right swimming fish</remarks>
    class FramesetPair : Tuple<Frameset, Frameset>, IDisposable
    {
        public FramesetPair(Frameset left, Frameset right)
            : base(left, right)
        {
            Debug.Assert(left != null);
            Debug.Assert(right != null);
        }

        public int FrameWidth
        {
            get
            {
                return Item1.FrameWidth;
            }
        }

        public int FrameHeight
        {
            get
            {
                return Item1.FrameHeight;
            }
        }

        public int Count
        {
            get
            {
                return Item1.Count;
            }
        }

        public void Dispose()
        {
            if (Item1 != null)
                Item1.Dispose();

            if (Item2 != null)
                Item2.Dispose();
        }
    }
}
