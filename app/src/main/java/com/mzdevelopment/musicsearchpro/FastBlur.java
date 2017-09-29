package com.mzdevelopment.musicsearchpro;

import android.graphics.Bitmap;
import java.lang.reflect.Array;

public class FastBlur
{

    public FastBlur()
    {
    }

    public static Bitmap doBlur(Bitmap bitmap, int i, boolean flag)
    {
        Bitmap bitmap1;
        if(flag)
        {
            bitmap1 = bitmap;
        } else
        {
            bitmap1 = bitmap.copy(bitmap.getConfig(), true);
        }
        if(i < 1)
        {
            return null;
        }
        int j = bitmap1.getWidth();
        int k = bitmap1.getHeight();
        int ai[] = new int[j * k];
        bitmap1.getPixels(ai, 0, j, 0, 0, j, k);
        int l = j - 1;
        int i1 = k - 1;
        int j1 = j * k;
        int k1 = 1 + (i + i);
        int ai1[] = new int[j1];
        int ai2[] = new int[j1];
        int ai3[] = new int[j1];
        int ai4[] = new int[Math.max(j, k)];
        int l1 = k1 + 1 >> 1;
        int i2 = l1 * l1;
        int ai5[] = new int[i2 * 256];
        int j2 = 0;
        do
        {
            int k2 = i2 * 256;
            if(j2 >= k2)
            {
                break;
            }
            ai5[j2] = j2 / i2;
            j2++;
        } while(true);
        int l2 = 0;
        int i3 = 0;
        int ai6[] = {
            k1, 3
        };
        int ai7[][] = (int[][])Array.newInstance(Integer.TYPE, ai6);
        int j3 = i + 1;
        for(int k3 = 0; k3 < k; k3++)
        {
            int k10 = 0;
            int l10 = 0;
            int i11 = 0;
            int j11 = 0;
            int k11 = 0;
            int l11 = 0;
            int i12 = 0;
            int j12 = 0;
            int k12 = 0;
            int l12 = -i;
            while(l12 <= i) 
            {
                int i16 = ai[l2 + Math.min(l, Math.max(l12, 0))];
                int ai13[] = ai7[l12 + i];
                ai13[0] = (0xff0000 & i16) >> 16;
                ai13[1] = (0xff00 & i16) >> 8;
                ai13[2] = i16 & 0xff;
                int j16 = j3 - Math.abs(l12);
                i11 += j16 * ai13[0];
                l10 += j16 * ai13[1];
                k10 += j16 * ai13[2];
                if(l12 > 0)
                {
                    k12 += ai13[0];
                    j12 += ai13[1];
                    i12 += ai13[2];
                } else
                {
                    l11 += ai13[0];
                    k11 += ai13[1];
                    j11 += ai13[2];
                }
                l12++;
            }
            int i13 = i;
            for(int j13 = 0; j13 < j; j13++)
            {
                ai1[l2] = ai5[i11];
                ai2[l2] = ai5[l10];
                ai3[l2] = ai5[k10];
                int k13 = i11 - l11;
                int l13 = l10 - k11;
                int i14 = k10 - j11;
                int ai11[] = ai7[(k1 + (i13 - i)) % k1];
                int j14 = l11 - ai11[0];
                int k14 = k11 - ai11[1];
                int l14 = j11 - ai11[2];
                if(k3 == 0)
                {
                    ai4[j13] = Math.min(1 + (j13 + i), l);
                }
                int i15 = ai[i3 + ai4[j13]];
                ai11[0] = (0xff0000 & i15) >> 16;
                ai11[1] = (0xff00 & i15) >> 8;
                ai11[2] = i15 & 0xff;
                int j15 = k12 + ai11[0];
                int k15 = j12 + ai11[1];
                int l15 = i12 + ai11[2];
                i11 = k13 + j15;
                l10 = l13 + k15;
                k10 = i14 + l15;
                i13 = (i13 + 1) % k1;
                int ai12[] = ai7[i13 % k1];
                l11 = j14 + ai12[0];
                k11 = k14 + ai12[1];
                j11 = l14 + ai12[2];
                k12 = j15 - ai12[0];
                j12 = k15 - ai12[1];
                i12 = l15 - ai12[2];
                l2++;
            }

            i3 += j;
        }

        for(int l3 = 0; l3 < j; l3++)
        {
            int i4 = 0;
            int j4 = 0;
            int k4 = 0;
            int l4 = 0;
            int i5 = 0;
            int j5 = 0;
            int k5 = 0;
            int l5 = 0;
            int i6 = 0;
            int j6 = j * -i;
            int k6 = -i;
            while(k6 <= i) 
            {
                int i10 = l3 + Math.max(0, j6);
                int ai10[] = ai7[k6 + i];
                ai10[0] = ai1[i10];
                ai10[1] = ai2[i10];
                ai10[2] = ai3[i10];
                int j10 = j3 - Math.abs(k6);
                k4 += j10 * ai1[i10];
                j4 += j10 * ai2[i10];
                i4 += j10 * ai3[i10];
                if(k6 > 0)
                {
                    i6 += ai10[0];
                    l5 += ai10[1];
                    k5 += ai10[2];
                } else
                {
                    j5 += ai10[0];
                    i5 += ai10[1];
                    l4 += ai10[2];
                }
                if(k6 < i1)
                {
                    j6 += j;
                }
                k6++;
            }
            int l6 = l3;
            int i7 = i;
            for(int j7 = 0; j7 < k; j7++)
            {
                ai[l6] = 0xff000000 & ai[l6] | ai5[k4] << 16 | ai5[j4] << 8 | ai5[i4];
                int k7 = k4 - j5;
                int l7 = j4 - i5;
                int i8 = i4 - l4;
                int ai8[] = ai7[(k1 + (i7 - i)) % k1];
                int j8 = j5 - ai8[0];
                int k8 = i5 - ai8[1];
                int l8 = l4 - ai8[2];
                if(l3 == 0)
                {
                    ai4[j7] = j * Math.min(j7 + j3, i1);
                }
                int i9 = l3 + ai4[j7];
                ai8[0] = ai1[i9];
                ai8[1] = ai2[i9];
                ai8[2] = ai3[i9];
                int j9 = i6 + ai8[0];
                int k9 = l5 + ai8[1];
                int l9 = k5 + ai8[2];
                k4 = k7 + j9;
                j4 = l7 + k9;
                i4 = i8 + l9;
                i7 = (i7 + 1) % k1;
                int ai9[] = ai7[i7];
                j5 = j8 + ai9[0];
                i5 = k8 + ai9[1];
                l4 = l8 + ai9[2];
                i6 = j9 - ai9[0];
                l5 = k9 - ai9[1];
                k5 = l9 - ai9[2];
                l6 += j;
            }

        }

        bitmap1.setPixels(ai, 0, j, 0, 0, j, k);
        return bitmap1;
    }
}
