package com.mzdevelopment.musicsearchpro.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mzdevelopment.musicsearchpro.R;
import com.mzdevelopment.musicsearchpro.util.UiUtil;

public class FontableTextView extends TextView
{

    public FontableTextView(Context context)
    {
        super(context);
    }

    public FontableTextView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        UiUtil.setCustomFont(this, context, attributeset, R.styleable.com_sparte_freemusicstream_view_FontableTextView, 0);
    }

    public FontableTextView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        UiUtil.setCustomFont(this, context, attributeset, R.styleable.com_sparte_freemusicstream_view_FontableTextView, 0);
    }
}
